package org.karamelsoft.axon.demo.services.cards.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.Instant

sealed interface CardEvent {
    val cardId: CardId
    val timestamp: Instant
}

data class CardCreated(
    override val cardId: CardId,
    val validity: CardValidity,
    val account: CardAccount,
    val owner: CardOwner,
    override val timestamp: Instant
): CardEvent

data class CardBlocked(
    override val cardId: CardId,
    override val timestamp: Instant
): CardEvent

data class CardPinCodeSetup(
    override val cardId: CardId,
    val pinCode: CardPinCode,
    override val timestamp: Instant
): CardEvent

data class CardPinCodeChanged(
    override val cardId: CardId,
    val newPinCode: CardPinCode,
    override val timestamp: Instant
): CardEvent

data class CardPinCodeValidated(
    override val cardId: CardId,
    override val timestamp: Instant
): CardEvent

data class CardPinCodeValidationFailed(
    override val cardId: CardId,
    override val timestamp: Instant
): CardEvent
