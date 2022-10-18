package org.karamelsoft.axon.bank.interfaces.http.accounts

import org.karamelsoft.axon.bank.interfaces.http.Event
import org.karamelsoft.axon.bank.services.accounts.api.*
import java.time.Instant

interface AccountAction {
    val timestamp: Instant
}

data class AmountDeposit(
    val amount: Double,
    val from: String,
    val description: String? = null,
    override val timestamp: Instant = Instant.now()
) : AccountAction

data class AmountWithdrawal(
    val amount: Double,
    val to: String,
    val description: String? = null,
    override val timestamp: Instant = Instant.now()
) : AccountAction

data class CreditLineOpening(
    val amount: Double,
    override val timestamp: Instant = Instant.now()
) : AccountAction

data class CreditLineIncrease(
    val amount: Double,
    override val timestamp: Instant = Instant.now()
) : AccountAction

data class CreditLineDecrease(
    val amount: Double,
    override val timestamp: Instant = Instant.now()
) : AccountAction

data class CreditLineClosing(
    override val timestamp: Instant = Instant.now()
) : AccountAction

data class AccountFrame(
    val event: Event,
    val state: AccountState
) {
    fun aggregate(frame: AccountFrame) = AccountFrame(
        event = frame.event,
        state = state.handle(frame.event)
    )

    companion object {
        fun from(payload: Any) = AccountFrame(
            event = Event.from(payload),
            state = AccountState.empty()
        )
    }
}

data class AccountState(
    val balance: Double,
    val closed: Boolean,
    val creditLineOpened: Boolean,
    val creditLineAmount: Double
) {
    companion object {
        fun empty() = AccountState(0.0, false, false , 0.0)
    }

    fun handle(event: Any): AccountState = when (event) {
        is Event -> handle(event.payload)
        is NewAccountOpened -> empty()
        is AmountDeposited -> deposited(event.amount)
        is AmountWithdrew -> withdrew(event.amount)
        is CreditLineOpened -> creditLineOpened(event.amount)
        is AccountClosed -> closed()
        else -> throw IllegalStateException("unknown event: ${event.javaClass.canonicalName}")
    }

    private fun deposited(amount: Double) = copy(balance = balance + amount)
    private fun withdrew(amount: Double) = copy(balance = balance - amount)
    private fun creditLineOpened(amount: Double) = copy(creditLineOpened = true, creditLineAmount = amount)
    private fun closed() = copy(closed = true)
}
