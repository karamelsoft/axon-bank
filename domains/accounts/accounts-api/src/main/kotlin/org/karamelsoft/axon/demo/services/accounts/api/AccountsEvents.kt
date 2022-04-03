package org.karamelsoft.axon.demo.services.accounts.api

import org.karamelsoft.research.axon.libraries.service.api.Event
import java.time.Instant

sealed interface AccountEvent: Event {
    val accountId: AccountId
}

data class NewAccountRegistered(
    override val accountId: AccountId,
    val owner: AccountOwner,
    override val timestamp: Instant = Instant.now()
): AccountEvent

data class AmountDeposited(
    override val accountId: AccountId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): AccountEvent

data class AmountWithdrew(
    override val accountId: AccountId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): AccountEvent

data class AccountClosed(
    override val accountId: AccountId,
    override val timestamp: Instant = Instant.now()
): AccountEvent