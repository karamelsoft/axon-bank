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
    private lateinit var customerId: CustomerId

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun <T> handle(command: RegisterNewCustomer) = Status.of {
        AggregateLifecycle.apply(
            NewCustomerRegistered(
                customerId = command.customerId,
                details = command.details,
                address = command.address,
                timestamp = command.timestamp
            )
        )
    }

    @EventSourcingHandler
    fun on(event: NewCustomerRegistered) {
        customerId = event.customerId
    }

    @CommandHandler
    fun handle(command: CorrectCustomerDetails) = Status.of {
        AggregateLifecycle.apply(
            CustomerDetailsCorrected(
                customerId = customerId,
                newDetails = command.newDetails,
                timestamp = command.timestamp
            )
        )
    }

    @CommandHandler
    fun handle(command: CorrectCustomerAddress) = Status.of {
        AggregateLifecycle.apply(
            CustomerAddressCorrected(
                customerId = customerId,
                newAddress = command.newAddress,
                timestamp = command.timestamp
            )
        )
    }
}