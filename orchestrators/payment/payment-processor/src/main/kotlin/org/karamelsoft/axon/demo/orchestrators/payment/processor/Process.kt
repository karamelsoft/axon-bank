package org.karamelsoft.axon.demo.orchestrators.payment.processor

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.karamelsoft.axon.demo.orchestrators.payment.api.PayByCard
import org.karamelsoft.axon.demo.services.accounts.api.AccountId
import org.karamelsoft.axon.demo.services.accounts.api.TransferAmount
import org.karamelsoft.axon.demo.services.cards.api.CardAssignments
import org.karamelsoft.axon.demo.services.cards.api.UseCard
import org.karamelsoft.research.axon.libraries.service.api.Status
import org.karamelsoft.research.axon.libraries.service.api.andThen
import org.springframework.stereotype.Component

@Component
class PaymentByCardProcess(val commandGateway: ReactorCommandGateway) {

    @CommandHandler
    fun handle(command: PayByCard): Status<Unit> {
        fun useCard() = UseCard(
            cardId = command.cardId,
            pinCode = command.pinCode
        )

        fun transferAmount(cardAssignments: CardAssignments) = TransferAmount(
            from = AccountId(number = cardAssignments.account.reference),
            to = command.destination,
            amount = command.amount,
            description = command.description
        )

        return commandGateway.send<Status<CardAssignments>>(useCard())
            .andThen { commandGateway.send<Status<Unit>>(transferAmount(it)) }
            .block()!!
    }
}
