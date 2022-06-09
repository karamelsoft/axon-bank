package org.karamelsoft.axon.demo.orchestrators.payment.api

import org.axonframework.commandhandling.RoutingKey
import org.karamelsoft.axon.demo.services.accounts.api.AccountId
import org.karamelsoft.axon.demo.services.cards.api.CardId
import org.karamelsoft.axon.demo.services.cards.api.CardPinCode
import org.karamelsoft.research.axon.libraries.service.api.Command
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
