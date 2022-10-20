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
    val newThreshold: Double,
    override val timestamp: Instant = Instant.now()
): CreditLineEvent

data class CreditLineDecreased(
    override val creditLineId: CreditLineId,
    val newThreshold: Double,
    override val timestamp: Instant = Instant.now()
): CreditLineEvent

data class CreditLineClosed(
    override val creditLineId: CreditLineId,
    override val timestamp: Instant = Instant.now()
): CreditLineEvent
