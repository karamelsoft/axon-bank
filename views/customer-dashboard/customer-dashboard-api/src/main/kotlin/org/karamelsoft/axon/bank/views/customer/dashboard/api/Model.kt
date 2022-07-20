package org.karamelsoft.axon.bank.views.customer.dashboard.api

import java.time.Instant
import java.time.LocalDate
import java.util.*

data class CustomerId(val value: UUID) {
    companion object {
        fun from(customerId: String) = CustomerId(UUID.fromString(customerId))
    }
}

data class CustomerDashboard(
    val id: UUID = UUID.randomUUID(),
    val customerId: String,
    val accountId: String? = null,
    val cardId: String? = null,
    val customer: CustomerProjection? = null,
    val account: AccountProjection? = null,
    val card: CardProjection? = null,
    val creationTimestamp: Instant = Instant.now(),
    val lastModificationTimestamp: Instant = creationTimestamp
)

data class CustomerProjection(
    val details: CustomerDetails,
    val address: CustomerAddress,
    val creationTimestamp: Instant = Instant.now(),
    val lastModificationTimestamp: Instant = creationTimestamp
)

data class CustomerDetails(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate
) {
    fun toQueryAPI() = CustomerDetails(
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate
    )
}

data class CustomerAddress(
    val street: String,
    val number: String,
    val postalCode: String,
    val city: String,
    val country: String
) {
    fun toQueryAPI() = CustomerAddress(
        street = street,
        number = number,
        postalCode = postalCode,
        city = city,
        country = country
    )
}

data class AccountProjection(
    val status: AccountStatus,
    val balance: Double,
    val creationTimestamp: Instant = Instant.now(),
    val lastModificationTimestamp: Instant = creationTimestamp
)

enum class AccountStatus {
    OPENED,
    CLOSED
}

data class CardProjection(
    val status: CardStatus,
    val creationTimestamp: Instant = Instant.now(),
    val lastModificationTimestamp: Instant = creationTimestamp
)

enum class CardStatus {
    ACTIVE,
    BLOCKED
}
