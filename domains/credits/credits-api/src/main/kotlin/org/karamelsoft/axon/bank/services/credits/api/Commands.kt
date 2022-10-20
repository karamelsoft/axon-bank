package org.karamelsoft.axon.bank.services.credits.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.Instant

sealed interface CreditLineCommand {
    val creditLineId: CreditLineId
    val timestamp: Instant
}

data class OpenCreditLine(
    @TargetAggregateIdentifier
    override val creditLineId: CreditLineId,
    val threshold: Double,
    val validity: CreditLineValidity,
    val creditor: CreditorAccount,
    override val timestamp: Instant = Instant.now()
): CreditLineCommand

data class IncreaseCreditLine(
    @TargetAggregateIdentifier
    override val creditLineId: CreditLineId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): CreditLineCommand

data class DecreaseCreditLine(
    @TargetAggregateIdentifier
    override val creditLineId: CreditLineId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): CreditLineCommand

data class CloseCreditLine(
    @TargetAggregateIdentifier
    override val creditLineId: CreditLineId,
    override val timestamp: Instant = Instant.now()
): CreditLineCommand