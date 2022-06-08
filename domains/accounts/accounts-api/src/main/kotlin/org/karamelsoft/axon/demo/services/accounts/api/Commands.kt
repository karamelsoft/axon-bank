package org.karamelsoft.axon.demo.services.accounts.api

import org.axonframework.commandhandling.RoutingKey
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.karamelsoft.research.axon.libraries.service.api.Command
import java.time.Instant

sealed interface AccountCommand : Command {
    val accountId: AccountId
}

data class RegisterNewAccount(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val owner: AccountOwner,
    override val timestamp: Instant = Instant.now()
) : AccountCommand

data class DepositAmount(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val amount: Double,
    val from: String,
    val description: String? = null,
    override val timestamp: Instant = Instant.now()
) : AccountCommand

data class WithdrawAmount(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val amount: Double,
    val to: String,
    val description: String? = null,
    override val timestamp: Instant = Instant.now()
) : AccountCommand

data class CloseAccount(
    @TargetAggregateIdentifier override val accountId: AccountId,
    val description: String? = null,
    override val timestamp: Instant = Instant.now()
): AccountCommand

data class TransferAmount(
    @RoutingKey
    val from: AccountId,
    val to: AccountId,
    val amount: Double,
    val description: String? = null,
    override val timestamp: Instant = Instant.now()
): Command
