package org.karamelsoft.axon.demo.interfaces.http.cards

import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.karamelsoft.axon.demo.interfaces.http.Event
import org.karamelsoft.axon.demo.services.cards.api.*
import org.karamelsoft.research.axon.libraries.service.api.Status
import org.karamelsoft.research.axon.libraries.service.module.readEvents
import org.karamelsoft.axon.demo.interfaces.http.handleStatus
import org.karamelsoft.axon.demo.orchestrators.payment.api.PayByCard
import org.karamelsoft.axon.demo.services.accounts.api.AccountId
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/cards")
class CardController(
    val commandGateway: ReactorCommandGateway,
    val eventStore: EventStore
) {

    @PostMapping("/{card_id}/block")
    fun blockCard(@PathVariable("card_id") cardId: String): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(BlockCard(CardId(cardId))).handleStatus()
    }

    @GetMapping("/{card_id}/history")
    fun getHistory(@PathVariable("card_id") cardId: String): Flux<Event> {
        return eventStore.readEvents(CardId(cardId))
            .map { domainEvent -> Event.from(domainEvent.payload) }
    }

    @PutMapping("/{card_id}/pin/setup")
    fun setCardPinCode(@PathVariable("card_id") cardId: String, @RequestBody action: CardSetup): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            SetupCardPinCode(
                cardId = CardId(cardId),
                pinCode = CardPinCode.of(action.pinCode),
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PostMapping("/{card_id}/pin/change")
    fun changeCardPin(@PathVariable("card_id") cardId: String, @RequestBody action: CardPinChange): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            ChangeCardPinCode(
                cardId = CardId(cardId),
                currentPinCode = CardPinCode.of(action.currentPinCode),
                newPinCode = CardPinCode.of(action.newPinCode),
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PostMapping("/{card_id}/pin/validate")
    fun validateCardPinCode(
        @PathVariable("card_id") cardId: String,
        @RequestBody action: CardPinValidation
    ): Mono<CardAssignments> {
        return commandGateway.send<Status<CardAssignments>>(
            UseCard(
                cardId = CardId(cardId),
                pinCode = CardPinCode.of(action.currentPinCode),
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PostMapping("/{card_id}/payment")
    fun payByCard(
        @PathVariable("card_id") cardId: String,
        @RequestBody action: CardPayment
    ): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            PayByCard(
                cardId = CardId(cardId),
                pinCode = CardPinCode.of(action.pinCode),
                to = AccountId(action.destination),
                amount = action.amount,
                description = action.description,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }
}
