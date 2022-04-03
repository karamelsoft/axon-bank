package org.karamelsoft.axon.demo.services.customers.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.Instant

sealed interface CustomerCommand {
    val customerId: CustomerId
    val timestamp: Instant
}

data class RegisterNewCustomer(
    @TargetAggregateIdentifier override val customerId: CustomerId,
    val details: CustomerDetails,
    val address: CustomerAddress,
    override val timestamp: Instant
): CustomerCommand

data class CorrectCustomerDetails(
    @TargetAggregateIdentifier override val customerId: CustomerId,
    val newDetails: CustomerDetails,
    override val timestamp: Instant
): CustomerCommand

data class CorrectCustomerAddress(
    @TargetAggregateIdentifier override val customerId: CustomerId,
    val newAddress: CustomerAddress,
    override val timestamp: Instant
): CustomerCommand

