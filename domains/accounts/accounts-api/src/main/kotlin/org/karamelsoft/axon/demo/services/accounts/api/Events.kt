package org.karamelsoft.axon.demo.services.accounts.api

import org.karamelsoft.research.axon.libraries.service.api.Event
import java.time.Instant
import java.util.*

sealed interface AccountEvent: Event {
    val accountId: AccountId
}

data class NewAccountOpened(
    override val accountId: AccountId,
    val owner: AccountOwner,
    override val timestamp: Instant = Instant.now()
): AccountEvent

data class AmountDeposited(
    override val accountId: AccountId,
    val amount: Double,
    val from: AccountId,
    val description: String? = null,
    val operationId: UUID = UUID.randomUUID(),
    override val timestamp: Instant = Instant.now()
): AccountEvent

data class AmountWithdrew(
    override val accountId: AccountId,
    val amount: Double,
    val to: AccountId,
    val description: String? = null,
    val operationId: UUID = UUID.randomUUID(),
    override val timestamp: Instant = Instant.now()
): AccountEvent

data class AccountClosed(
    override val accountId: AccountId,
    val description: String? = null,
    override val timestamp: Instant = Instant.now()
): AccountEvent
