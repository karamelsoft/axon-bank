package org.karamelsoft.research.axon.libraries.service.api

import org.axonframework.modelling.command.AggregateLifecycle
import java.lang.Exception
import java.time.LocalDateTime

interface Command {
    val timestamp: LocalDateTime

    fun toEvents(timestamp: LocalDateTime = LocalDateTime.now()): List<Event>

    fun accepted(timestamp: LocalDateTime = LocalDateTime.now()) {
        toEvents(timestamp).forEach { event -> AggregateLifecycle.apply(event) }
    }

    fun rejectedBy(exception: Exception) {
        throw exception
    }
}

interface Event {
    val timestamp: LocalDateTime
}
