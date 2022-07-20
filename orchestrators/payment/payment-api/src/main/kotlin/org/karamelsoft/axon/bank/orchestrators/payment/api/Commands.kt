package org.karamelsoft.axon.bank.orchestrators.payment.api

import org.axonframework.commandhandling.RoutingKey
import org.karamelsoft.axon.bank.services.accounts.api.AccountId
import org.karamelsoft.axon.bank.services.cards.api.CardId
import org.karamelsoft.axon.bank.services.cards.api.CardPinCode
import org.karamelsoft.research.axon.libraries.artifacts.api.Command
import java.time.Instant
import java.util.*

data class PayByCard(
    @RoutingKey
    val cardId: CardId,
    val pinCode: CardPinCode,
    val to: AccountId,
    val amount: Double,
    val description: String? = null,
    val operationId: UUID = UUID.randomUUID(),
    override val timestamp: Instant = Instant.now()
): Command
