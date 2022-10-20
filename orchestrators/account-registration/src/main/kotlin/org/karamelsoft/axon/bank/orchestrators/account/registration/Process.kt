package org.karamelsoft.axon.bank.orchestrators.account.registration

import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.karamelsoft.axon.bank.services.accounts.api.AccountId
import org.karamelsoft.axon.bank.services.accounts.api.AccountOwner
import org.karamelsoft.axon.bank.services.accounts.api.NewAccountOpened
import org.karamelsoft.axon.bank.services.cards.api.*
import org.karamelsoft.axon.bank.services.credits.api.CreditLineId
import org.karamelsoft.axon.bank.services.credits.api.CreditLineValidity
import org.karamelsoft.axon.bank.services.credits.api.CreditorAccount
import org.karamelsoft.axon.bank.services.credits.api.OpenCreditLine
import org.karamelsoft.research.axon.libraries.artifacts.api.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import java.time.Period

private const val d = 200.0

@Saga
class AccountRegistrationProcess {

    @Transient
    private val logger: Logger = LoggerFactory.getLogger(AccountRegistrationProcess::class.java)

    @Autowired
    private lateinit var commandGateway: ReactorCommandGateway

    private lateinit var accountId: AccountId
    private lateinit var accountOwner: AccountOwner
    private lateinit var cardId: CardId
    private lateinit var creditLineId: CreditLineId

    @StartSaga
    @SagaEventHandler(associationProperty = "accountId")
    fun handle(event: NewAccountOpened) {
        logger.info("New account registration should create a card")
        accountId = event.accountId
        accountOwner = event.owner

        creditLineId = CreditLineId()
        SagaLifecycle.associateWith("creditLineId", creditLineId.toString())
        commandGateway.send<Status<CreditLineId>>(
            OpenCreditLine(
                creditLineId = creditLineId,
                200.0,
                CreditLineValidity(LocalDateTime.now(), Period.ofMonths(1)),
                creditor = CreditorAccount(accountId.toString())
            )
        ).block()

        cardId = CardId()
        SagaLifecycle.associateWith("cardId", cardId.toString())
        commandGateway.send<Status<Unit>>(
            CreateCard(
                cardId = cardId,
                account = CardAccount(accountId.number),
                owner = CardOwner(accountOwner.reference),
                validity = CardValidity()
            )
        ).block()
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "cardId")
    fun handle(event: CardCreated) {
        logger.info("Card created")
    }
}
