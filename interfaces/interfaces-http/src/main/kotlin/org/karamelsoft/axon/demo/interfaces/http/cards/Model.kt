package org.karamelsoft.axon.demo.interfaces.http.cards

import org.karamelsoft.axon.demo.services.cards.api.CardAccount
import org.karamelsoft.axon.demo.services.cards.api.CardOwner
import org.karamelsoft.axon.demo.services.cards.api.CardValidity
import java.time.Instant

interface CardAction {
    val timestamp: Instant
}

data class CardRegistration(
    val validity: CardValidity,
    val account: CardAccount,
    val owner: CardOwner,
    override val timestamp: Instant
): CardAction
