package org.karamelsoft.axon.demo.interfaces.http.cards

import org.karamelsoft.axon.demo.services.cards.api.CardPinCode
import java.time.Instant

interface CardAction {
    val timestamp: Instant
}

data class CardSetup(
    val pinCode: String,
    override val timestamp: Instant = Instant.now()
): CardAction

data class CardPinChange(
    val currentPinCode: String,
    val newPinCode: String,
    override val timestamp: Instant = Instant.now()
): CardAction

data class CardPinValidation(
    val currentPinCode: String,
    override val timestamp: Instant = Instant.now()
): CardAction
