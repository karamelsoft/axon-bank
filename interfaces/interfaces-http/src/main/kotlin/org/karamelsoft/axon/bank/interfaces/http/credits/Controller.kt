package org.karamelsoft.axon.bank.interfaces.http.credits

import org.axonframework.eventsourcing.eventstore.EventStore
import org.karamelsoft.axon.bank.interfaces.http.Event
import org.karamelsoft.axon.bank.services.credits.api.CreditLineId
import org.karamelsoft.research.axon.libraries.artifacts.module.readEvents
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/credits")
class CreditController(val eventStore: EventStore) {

    @GetMapping("/{creditLineId}/history")
    fun getHistory(creditLineId: String): Flux<Event> {
        return eventStore.readEvents(CreditLineId.from(creditLineId))
            .map { domainEvent -> Event.from(domainEvent.payload) }
    }

}