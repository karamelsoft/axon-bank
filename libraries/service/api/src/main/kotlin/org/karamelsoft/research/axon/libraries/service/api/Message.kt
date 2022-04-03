package org.karamelsoft.research.axon.libraries.service.api

import java.time.Instant

interface Command {
    val timestamp: Instant
}

interface Event {
    val timestamp: Instant
}
