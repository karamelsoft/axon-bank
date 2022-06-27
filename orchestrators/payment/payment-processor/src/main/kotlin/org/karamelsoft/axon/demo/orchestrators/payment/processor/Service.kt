package org.karamelsoft.axon.demo.orchestrators.payment.processor

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.karamelsoft.axon.demo.orchestrators.payment.api.PayByCard
import org.karamelsoft.axon.demo.services.accounts.api.AccountId
import org.karamelsoft.axon.demo.services.accounts.api.WithdrawAmount
import org.karamelsoft.axon.demo.services.cards.api.CardAssignments
import org.karamelsoft.axon.demo.services.cards.api.UseCard
import org.karamelsoft.research.axon.libraries.service.api.Status
import org.karamelsoft.research.axon.libraries.service.api.andThenMono
import org.springframework.stereotype.Component

@Component
class PaymentService(val commandGateway: ReactorCommandGateway) {

    @CommandHandler
    fun handle(command: PayByCard): Status<Unit> {
        fun useCard() = UseCard(
            cardId = command.cardId,
            pinCode = command.pinCode
        )

        fun withdrawAmount(cardAssignments: CardAssignments) = WithdrawAmount(
            accountId = AccountId(cardAssignments.account.reference),
            amount = command.amount,
            to = command.to,
            description = command.description,
            operationId = command.operationId
        )

        return commandGateway.send<Status<CardAssignments>>(useCard())
            .andThenMono { commandGateway.send<Status<Unit>>(withdrawAmount(it)) }
            .block()!!
    }
}
