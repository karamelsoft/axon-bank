package org.karamelsoft.axon.bank.services.credits.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import org.karamelsoft.axon.bank.services.credits.api.*
import org.karamelsoft.research.axon.libraries.artifacts.api.Status

@Aggregate
internal class CreditLine() {

    @AggregateIdentifier
    private lateinit var id: CreditLineId
    private var threshold: Double = 0.0
    private lateinit var validity: CreditLineValidity
    private lateinit var creditor: CreditorAccount

    private var outstanding: Double = 0.0

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun handle(command: OpenCreditLine): Status<CreditLineId> = Status.of {
        AggregateLifecycle.apply(
            CreditLineOpened(
                creditLineId = command.creditLineId,
                threshold = command.threshold,
                validity = command.validity,
                creditor = command.creditor,
                timestamp = command.timestamp
            )
        )
        command.creditLineId
    }

    @EventSourcingHandler
    fun on(event: CreditLineOpened) {
        id = event.creditLineId
        threshold = event.threshold
        validity = event.validity
        creditor = event.creditor
    }

}