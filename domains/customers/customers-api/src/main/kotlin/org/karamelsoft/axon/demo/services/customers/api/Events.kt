package org.karamelsoft.axon.demo.services.customers.api

import java.time.Instant

sealed interface CustomerEvent {
    val customerId: CustomerId
    val timestamp: Instant
}

data class NewCustomerRegistered(
    override val customerId: CustomerId,
    val details: CustomerDetails,
    val address: CustomerAddress,
    override val timestamp: Instant
): CustomerEvent

data class CustomerDetailsCorrected(
    override val customerId: CustomerId,
    val newDetails: CustomerDetails,
    override val timestamp: Instant
): CustomerEvent

data class CustomerAddressCorrected(
    override val customerId: CustomerId,
    val newAddress: CustomerAddress,
    override val timestamp: Instant
): CustomerEvent

