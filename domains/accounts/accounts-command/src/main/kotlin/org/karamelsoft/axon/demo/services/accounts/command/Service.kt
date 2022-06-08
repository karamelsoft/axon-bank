package org.karamelsoft.axon.demo.services.accounts.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.karamelsoft.axon.demo.services.accounts.api.DepositAmount
import org.karamelsoft.axon.demo.services.accounts.api.TransferAmount
import org.karamelsoft.axon.demo.services.accounts.api.WithdrawAmount
import org.karamelsoft.research.axon.libraries.service.api.Status
import org.karamelsoft.research.axon.libraries.service.api.andThen
import org.springframework.stereotype.Component

@Component
class TransferService(val commandGateway: ReactorCommandGateway) {

    @CommandHandler
    fun handle(command: TransferAmount): Status<Unit> {
        fun withdrawAmount() = WithdrawAmount(
            accountId = command.from,
            amount = command.amount,
            to = command.to.number,
            description = command.description
        )

        fun depositAmount() = DepositAmount(
            accountId = command.to,
            amount = command.amount,
            from = command.from.number,
            description = command.description
        )

        return commandGateway.send<Status<Unit>>(withdrawAmount())
            .andThen { commandGateway.send<Status<Unit>>(depositAmount()) }
            .block()!!
    }
}
