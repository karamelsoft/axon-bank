package org.karamelsoft.axon.bank.interfaces.http.credits

import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.karamelsoft.axon.bank.interfaces.http.Event
import org.karamelsoft.axon.bank.interfaces.http.handleStatus
import org.karamelsoft.axon.bank.services.credits.api.CloseCreditLine
import org.karamelsoft.axon.bank.services.credits.api.CreditLineId
import org.karamelsoft.axon.bank.services.credits.api.DecreaseCreditLine
import org.karamelsoft.axon.bank.services.credits.api.IncreaseCreditLine
import org.karamelsoft.research.axon.libraries.artifacts.api.Status
import org.karamelsoft.research.axon.libraries.artifacts.module.readEvents
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/credits")
class CreditController(val commandGateway: ReactorCommandGateway, val eventStore: EventStore) {

    @PostMapping("/{creditLineId}/increase")
    fun increase(
        @PathVariable creditLineId: String,
        @RequestBody body: CreditLineBody
    ): Mono<CreditLineResponse> {
        return commandGateway.send<Status<Double>>(
            IncreaseCreditLine(
                CreditLineId.from(creditLineId),
                amount = body.amount
            )
        )
            .handleStatus()
            .map { CreditLineResponse(creditLineId, it) }
    }

    @PostMapping("/{creditLineId}/decrease")
    fun decrease(
        @PathVariable creditLineId: String,
        @RequestBody body: CreditLineBody
    ): Mono<CreditLineResponse> {
        return commandGateway.send<Status<Double>>(
            DecreaseCreditLine(
                CreditLineId.from(creditLineId),
                amount = body.amount
            )
        )
            .handleStatus()
            .map { CreditLineResponse(creditLineId, it) }
    }

    @PostMapping("/{creditLineId}/close")
    fun close(
        @PathVariable creditLineId: String,
    ): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            CloseCreditLine(CreditLineId.from(creditLineId))
        ).handleStatus()
    }

    @GetMapping("/{creditLineId}/history")
    fun getHistory(@PathVariable creditLineId: String): Flux<Event> {
        return eventStore.readEvents(CreditLineId.from(creditLineId))
            .map { domainEvent -> Event.from(domainEvent.payload) }
    }

}