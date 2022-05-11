package org.karamelsoft.research.axon.libraries.service.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

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
