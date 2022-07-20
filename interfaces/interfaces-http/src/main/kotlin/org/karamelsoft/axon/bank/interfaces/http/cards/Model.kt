package org.karamelsoft.axon.bank.interfaces.http.cards

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

data class CardPayment(
    val pinCode: String,
    val amount: Double,
    val destination: String,
    val description: String? = null,
    override val timestamp: Instant = Instant.now(),
): CardAction
