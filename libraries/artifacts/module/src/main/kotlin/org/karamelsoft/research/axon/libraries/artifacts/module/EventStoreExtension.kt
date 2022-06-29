package org.karamelsoft.research.axon.libraries.artifacts.module

import org.axonframework.eventsourcing.eventstore.EventStore
import reactor.core.publisher.Flux

fun EventStore.readEvents(aggregateId: Any) = Flux.fromStream(this.readEvents(aggregateId.toString()).asStream())
