package org.karamelsoft.axon.demo.services.customers.api

import java.time.LocalDate
import java.util.UUID

data class CustomerId(val value: UUID = UUID.randomUUID()) {
    companion object {
        fun of(value: String) = CustomerId(UUID.fromString(value))
    }

    fun asReference() = value.toString()
}

data class CustomerDetails(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate
)

data class CustomerAddress(
    val street: String,
    val number: String,
    val postalCode: String,
    val city: String,
    val country: String
)
