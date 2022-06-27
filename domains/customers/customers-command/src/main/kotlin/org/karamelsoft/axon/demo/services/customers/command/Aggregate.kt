package org.karamelsoft.axon.demo.services.customers.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import org.karamelsoft.axon.demo.services.customers.api.*
import org.karamelsoft.research.axon.libraries.service.api.Status

@Aggregate
internal class Customer {

    @AggregateIdentifier
    private lateinit var id: CustomerId

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun handle(command: RegisterNewCustomer): Status<CustomerId> = Status.of {
        AggregateLifecycle.apply(
            NewCustomerRegistered(
                customerId = command.customerId,
                details = command.details,
                address = command.address,
                timestamp = command.timestamp
            )
        )
        command.customerId
    }

    @EventSourcingHandler
    fun on(event: NewCustomerRegistered) {
        id = event.customerId
    }

    @CommandHandler
    fun handle(command: CorrectCustomerDetails): Status<Unit> = Status.of<Unit> {
        AggregateLifecycle.apply(
            CustomerDetailsCorrected(
                customerId = id,
                newDetails = command.newDetails,
                timestamp = command.timestamp
            )
        )
    }

    @CommandHandler
    fun handle(command: CorrectCustomerAddress): Status<Unit> = Status.of {
        AggregateLifecycle.apply(
            CustomerAddressCorrected(
                customerId = id,
                newAddress = command.newAddress,
                timestamp = command.timestamp
            )
        )
    }
}
