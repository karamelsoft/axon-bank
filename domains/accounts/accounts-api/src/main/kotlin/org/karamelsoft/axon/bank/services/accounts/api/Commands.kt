package org.karamelsoft.axon.bank.services.accounts.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.karamelsoft.research.axon.libraries.artifacts.api.Command
import java.time.Instant
import java.util.*

sealed interface AccountCommand : Command {
    val accountId: AccountId
}

data class OpenNewAccount(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val owner: AccountOwner,
    override val timestamp: Instant = Instant.now()
) : AccountCommand

data class DepositAmount(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val amount: Double,
    val from: AccountId,
    val description: String? = null,
    val operationId: UUID = UUID.randomUUID(),
    override val timestamp: Instant = Instant.now()
) : AccountCommand

data class WithdrawAmount(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val amount: Double,
    val to: AccountId,
    val description: String? = null,
    val operationId: UUID = UUID.randomUUID(),
    override val timestamp: Instant = Instant.now()
) : AccountCommand

data class CloseAccount(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val description: String? = null,
    override val timestamp: Instant = Instant.now()
): AccountCommand

data class OpenCreditLine(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): AccountCommand

data class IncreaseCreditLine(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): AccountCommand

data class DecreaseCreditLine(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): AccountCommand

data class CloseCreditLine(
    @TargetAggregateIdentifier override val accountId: AccountId,
    override val timestamp: Instant = Instant.now()
): AccountCommand

data class AmountBorrowed(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val amount: Double,
    override val timestamp: Instant = Instant.now()
): AccountCommand
