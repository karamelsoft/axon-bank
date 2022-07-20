package org.karamelsoft.axon.bank.services.cards.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.Instant

sealed interface CardCommand {
    val cardId: CardId
    val timestamp: Instant
}

data class CreateCard(
    @TargetAggregateIdentifier override val cardId: CardId,
    val validity: CardValidity,
    val account: CardAccount,
    val owner: CardOwner,
    override val timestamp: Instant = Instant.now()
): CardCommand

data class BlockCard(
    @TargetAggregateIdentifier override val cardId: CardId,
    override val timestamp: Instant = Instant.now()
): CardCommand

data class SetupCardPinCode(
    @TargetAggregateIdentifier override val cardId: CardId,
    val pinCode: CardPinCode,
    override val timestamp: Instant = Instant.now()
): CardCommand

data class ChangeCardPinCode(
    @TargetAggregateIdentifier override val cardId: CardId,
    val currentPinCode: CardPinCode,
    val newPinCode: CardPinCode,
    override val timestamp: Instant = Instant.now()
): CardCommand

data class UseCard(
    @TargetAggregateIdentifier override val cardId: CardId,
    val pinCode: CardPinCode,
    override val timestamp: Instant = Instant.now()
): CardCommand
