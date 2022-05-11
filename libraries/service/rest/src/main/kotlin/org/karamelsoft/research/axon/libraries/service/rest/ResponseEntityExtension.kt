package org.karamelsoft.research.axon.libraries.service.rest

import org.karamelsoft.research.axon.libraries.service.api.*
import reactor.core.publisher.Mono

fun <T : Any> Mono<Status<T>>.handleStatus(): Mono<T> = this.map { status ->
    when (status) {
        is Ok -> status.value
        is BadInput -> throw IllegalArgumentException(status.message)
        is BadRequest -> throw IllegalStateException(status.message)
        is NotFound -> throw NoSuchElementException(status.message)
        is UnknownError -> throw RuntimeException(status.message)
        else -> throw RuntimeException("unknown error")
    }
}
