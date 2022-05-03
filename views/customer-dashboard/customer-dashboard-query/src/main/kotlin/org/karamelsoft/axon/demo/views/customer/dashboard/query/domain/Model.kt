package org.karamelsoft.axon.demo.views.customer.dashboard.query.domain

import org.karamelsoft.axon.demo.services.accounts.api.AccountId
import org.karamelsoft.axon.demo.services.cards.api.CardId
import org.karamelsoft.axon.demo.views.customer.dashboard.api.AccountStatus
import org.karamelsoft.axon.demo.views.customer.dashboard.api.CustomerAddress
import org.karamelsoft.axon.demo.views.customer.dashboard.api.CustomerDetails
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document(collection = "customer_dashboard")
data class CustomerDashboard(
    @Id
    val id: UUID = UUID.randomUUID(),
    val customerId: String,
    val accountId: String? = null,
    val cardId: String? = null,
    val customer: CustomerProjection? = null,
    val account: AccountProjection? = null,
    val card: CardProjection? = null,
    val creationTimestamp: Instant = Instant.now(),
    val lastModificationTimestamp: Instant = creationTimestamp
) {
    fun registerCustomer(details: CustomerDetails, address: CustomerAddress, timestamp: Instant = Instant.now()) = copy(
        customer = customer.register(details, address, timestamp),
        creationTimestamp = timestamp,
        lastModificationTimestamp = timestamp
    )

    fun correctCustomerDetails(newDetails: CustomerDetails, timestamp: Instant = Instant.now()) = copy(
        customer = customer?.correctDetails(newDetails, timestamp),
        lastModificationTimestamp = timestamp
    )

    fun correctCustomerAddress(newAddress: CustomerAddress, timestamp: Instant = Instant.now()) = copy(
        customer = customer?.correctAddress(newAddress, timestamp),
        lastModificationTimestamp = timestamp
    )

    fun registerAccount(accountId: AccountId, timestamp: Instant = Instant.now()) = copy(
        accountId = accountId.number,
        account = account.register(),
        lastModificationTimestamp = timestamp
    )

    fun closeAccount(timestamp: Instant = Instant.now()) = copy(
        account = account?.close(),
        lastModificationTimestamp = timestamp
    )

    fun depositAmount(amount: Double, timestamp: Instant = Instant.now()) = copy(
        account = account?.deposit(amount),
        lastModificationTimestamp = timestamp
    )

    fun withdrawAmount(amount: Double, timestamp: Instant = Instant.now()) = copy(
        account = account?.withdraw(amount),
        lastModificationTimestamp = timestamp
    )

    fun registerCard(cardId: CardId, timestamp: Instant = Instant.now()) = copy(
        cardId = cardId.number,
        card = card.register(timestamp),
        lastModificationTimestamp = timestamp
    )

    fun blockCard(timestamp: Instant = Instant.now()) = copy(
        card = card?.block(),
        lastModificationTimestamp = timestamp
    )

    fun toQueryAPI() = org.karamelsoft.axon.demo.views.customer.dashboard.api.CustomerDashboard(
        id = id,
        customerId = customerId,
        accountId = accountId,
        cardId = cardId,
        customer = customer?.toQueryAPI(),
        account = account?.toQueryAPI(),
        card = card?.toQueryAPI(),
        creationTimestamp = creationTimestamp,
        lastModificationTimestamp = lastModificationTimestamp
    )
}

data class CustomerProjection(
    val details: CustomerDetails,
    val address: CustomerAddress,
    val creationTimestamp: Instant,
    val lastModificationTimestamp: Instant = creationTimestamp
) {
    fun correctDetails(newDetails: CustomerDetails, timestamp: Instant = Instant.now()) = copy(
        details = newDetails,
        lastModificationTimestamp = timestamp
    )

    fun correctAddress(newAddress: CustomerAddress, timestamp: Instant) = copy(
        address = newAddress,
        lastModificationTimestamp = timestamp
    )

    fun toQueryAPI() = org.karamelsoft.axon.demo.views.customer.dashboard.api.CustomerProjection(
        details = details.toQueryAPI(),
        address = address.toQueryAPI(),
        creationTimestamp = creationTimestamp,
        lastModificationTimestamp = lastModificationTimestamp
    )

}

fun CustomerProjection?.register(
    details: CustomerDetails,
    address: CustomerAddress,
    timestamp: Instant = Instant.now()
) = this
    ?: CustomerProjection(
        details = details,
        address = address,
        creationTimestamp = timestamp
    )

data class AccountProjection(
    val status: AccountStatus,
    val balance: Double,
    val creationTimestamp: Instant,
    val lastModificationTimestamp: Instant = creationTimestamp
) {
    fun deposit(amount: Double, timestamp: Instant = Instant.now()) = copy(
        balance = balance + amount,
        lastModificationTimestamp = timestamp
    )

    fun withdraw(amount: Double, timestamp: Instant = Instant.now()) = copy(
        balance = balance - amount,
        lastModificationTimestamp = timestamp
    )

    fun close(timestamp: Instant = Instant.now()) = copy(
        status = AccountStatus.CLOSED,
        lastModificationTimestamp = timestamp
    )

    fun toQueryAPI() = org.karamelsoft.axon.demo.views.customer.dashboard.api.AccountProjection(
        status = status,
        balance = balance,
        creationTimestamp = creationTimestamp,
        lastModificationTimestamp = lastModificationTimestamp
    )
}

fun AccountProjection?.register(timestamp: Instant = Instant.now()) = this
    ?: AccountProjection(
        status = AccountStatus.OPENED,
        balance = 0.0,
        creationTimestamp = timestamp,
    )

data class CardProjection(
    val status: CardStatus,
    val creationTimestamp: Instant,
    val lastModificationTimestamp: Instant = creationTimestamp
) {
    fun block(timestamp: Instant = Instant.now()) = copy(
        status = CardStatus.BLOCKED,
        lastModificationTimestamp = timestamp
    )

    fun toQueryAPI() = org.karamelsoft.axon.demo.views.customer.dashboard.api.CardProjection(
        status = status.toQueryAPI(),
        creationTimestamp = creationTimestamp,
        lastModificationTimestamp = lastModificationTimestamp
    )
}

fun CardProjection?.register(timestamp: Instant = Instant.now()) = this
    ?: CardProjection(
        status = CardStatus.ACTIVE,
        creationTimestamp = timestamp
    )

enum class CardStatus {
    ACTIVE,
    BLOCKED,;

    fun toQueryAPI() = org.karamelsoft.axon.demo.views.customer.dashboard.api.CardStatus.valueOf(name)
}
