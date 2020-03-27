package org.karamelsoft.research.axon.libraries.service.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.concurrent.CompletableFuture

fun <T> CompletableFuture<T>.toCommandResponse() = this.thenApply { it.toCommandResponse() }

fun <T> T.toCommandResponse() = when (this) {
    null -> ResponseEntity(HttpStatus.ACCEPTED)
    else -> ResponseEntity(this, HttpStatus.ACCEPTED)
}

fun <T> CompletableFuture<T>.toQueryResponse() = this.thenApply { it.toQueryResponse() }

fun <T> T.toQueryResponse() = when (this) {
    null -> ResponseEntity(HttpStatus.NOT_FOUND)
    else -> ResponseEntity(this, HttpStatus.OK)
}
