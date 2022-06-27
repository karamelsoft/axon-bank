package org.karamelsoft.axon.demo.services.customers.api

import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import org.axonframework.commandhandling.RoutingKey
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.Instant

private val hashFunction: HashFunction = Hashing.sha512()
private val defaultCharset = Charsets.UTF_8

sealed interface CustomerCommand {
    val customerId: CustomerId
    val timestamp: Instant
}

fun customerConstraint(details: CustomerDetails, address: CustomerAddress): String {
    return hashFunction.newHasher()
        .putString(details.firstName, defaultCharset)
        .putString(details.lastName, defaultCharset)
        .putString(details.birthDate.toString(), defaultCharset)
        .hash().asLong().toString()
}

data class RegisterUniqueCustomer(
    @RoutingKey val customerId: CustomerId,
    val details: CustomerDetails,
    val address: CustomerAddress,
    val timestamp: Instant = Instant.now()
)

data class RegisterNewCustomer(
    @TargetAggregateIdentifier override val customerId: CustomerId,
    val details: CustomerDetails,
    val address: CustomerAddress,
    override val timestamp: Instant = Instant.now(),
) : CustomerCommand

data class CorrectCustomerDetails(
    @TargetAggregateIdentifier override val customerId: CustomerId,
    val newDetails: CustomerDetails,
    override val timestamp: Instant = Instant.now(),
) : CustomerCommand

data class CorrectCustomerAddress(
    @TargetAggregateIdentifier override val customerId: CustomerId,
    val newAddress: CustomerAddress,
    override val timestamp: Instant = Instant.now(),
) : CustomerCommand
