package org.karamelsoft.research.axon.libraries.service.module

import org.axonframework.eventsourcing.eventstore.EventStore

interface Projection<E, P : Projection<E, P>> {
    val aggregateId: Any
    fun handle(event: E): P
}

fun <E, P : Projection<E, P>> EventStore.projectionFor(projection: P): P {
    return this.readEvents(projection.aggregateId.toString())
        .asSequence()
        .fold(projection) { proj, event -> proj.handle(event.payload as E)}
}