package org.karamelsoft.axon.demo.services.accounts.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import org.karamelsoft.axon.demo.services.accounts.api.*
import org.karamelsoft.research.axon.libraries.service.api.Status

@Aggregate(snapshotTriggerDefinition = "accountSnapshotter")
internal class Account() {

    @AggregateIdentifier
    private lateinit var accountId: AccountId

    private var balance: Double = 0.0
    private var closed: Boolean = false

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun handle(command: OpenNewAccount): Status<Unit> = Status.of<Unit> {
        AggregateLifecycle.apply(
            NewAccountOpened(
                accountId = command.accountId,
                owner = command.owner,
                timestamp = command.timestamp
            )
        )
    }

    @CommandHandler
    fun handle(command: DepositAmount): Status<Unit> =
        if (closed) accountClosed()
        else Status.of<Unit> {
            AggregateLifecycle.apply(
                AmountDeposited(
                    accountId = accountId,
                    amount = command.amount,
                    from = command.from,
                    description = command.description,
                    timestamp = command.timestamp,
                )
            )
        }

    @CommandHandler
    fun handle(command: WithdrawAmount): Status<Unit> = when {
        closed -> accountClosed()
        balance < command.amount -> notEnoughCredit()
        else -> Status.of<Unit> {
            AggregateLifecycle.apply(
                AmountWithdrew(
                    accountId = accountId,
                    amount = command.amount,
                    to = command.to,
                    description = command.description,
                    timestamp = command.timestamp,
                )
            )
        }
    }

    @CommandHandler
    fun handle(command: CloseAccount): Status<Unit> =
        if (closed) accountClosed()
        else Status.of<Unit> {
            AggregateLifecycle.apply(
                AccountClosed(
                    accountId = accountId,
                    timestamp = command.timestamp
                )
            )
        }

    @EventSourcingHandler
    fun on(event: NewAccountOpened) {
        accountId = event.accountId
        balance = 0.0
        closed = false
    }

    @EventSourcingHandler
    fun on(event: AmountDeposited) {
        balance += event.amount
    }

    @EventSourcingHandler
    fun on(event: AmountWithdrew) {
        balance -= event.amount
    }

    @EventSourcingHandler
    fun on(event: AccountClosed) {
        closed = true
    }

}
