package org.karamelsoft.axon.bank.services.credits.api

import java.time.Instant

sealed interface CreditLineEvent {
    val creditLineId: CreditLineId
    val timestamp: Instant
}

data class CreditLineOpened(
    override val creditLineId: CreditLineId,
    val threshold: Double,
    val validity: CreditLineValidity,
    val creditor: CreditorAccount,
    override val timestamp: Instant = Instant.now()
): CreditLineEvent

data class CreditLineIncreased(
    override val creditLineId: CreditLineId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): CreditLineEvent

data class CreditLineDecreased(
    override val creditLineId: CreditLineId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): CreditLineEvent

data class CreditLineClosed(
    override val creditLineId: CreditLineId,
    override val timestamp: Instant = Instant.now()
): CreditLineEvent

data class AmountBorrowed(
    override val creditLineId: CreditLineId,
    val value: Double,
    override val timestamp: Instant = Instant.now()
): CreditLineEvent

data class AmountReimbursed(
    override val creditLineId: CreditLineId,
    val value: Double,
    override val timestamp: Instant = Instant.now()
): CreditLineEvent
