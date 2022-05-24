package org.karamelsoft.axon.demo.interfaces.http

import org.karamelsoft.research.axon.libraries.service.api.*
import org.karamelsoft.research.axon.libraries.service.api.UnknownError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
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

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentHandler(exception: IllegalArgumentException): ResponseEntity<Any> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(exception.message)

    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateHandler(exception: IllegalStateException): ResponseEntity<Any> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(exception.message)

    @ExceptionHandler(NoSuchElementException::class)
    fun noSuchElementHandler(exception: NoSuchElementException): ResponseEntity<Any> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.message)

    @ExceptionHandler(RuntimeException::class)
    fun noSuchElementHandler(exception: RuntimeException): ResponseEntity<Any> =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(exception.message)
}
