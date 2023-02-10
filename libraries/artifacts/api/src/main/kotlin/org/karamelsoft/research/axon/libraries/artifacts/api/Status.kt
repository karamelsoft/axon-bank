package org.karamelsoft.research.axon.libraries.artifacts.api

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

sealed interface /**/Status<T> {
    companion object {
        fun <T> ok(provider: () -> T) = Ok(provider())
        fun <T> of(operation: () -> T) = try {
            Ok(operation())
        } catch (e: Exception) {
            LoggerFactory.getLogger(Status::class.java).error("operation failed", e)
            UnknownError(e)
        }

        fun <T, U> andThenMono(next: (Status<T>) -> Mono<Status<U>>): (Status<T>) -> Mono<Status<U>> = { status ->
            status.andThenMono { next(status) }
        }

        fun <T> peek(onSuccess: (T) -> Any, onFailure: () -> Any): (Status<T>) -> Status<T> = { status ->
            status.peek(onSuccess, onFailure)
        }
    }

    fun peek(onSuccess: (T) -> Any, onFailure: () -> Any): Status<T>

    fun <U> map(mapping: (T) -> U, or: () -> U): U
    fun <U> andThen(next: (T) -> Status<U>): Status<U>
    fun <U> andThenMono(next: (T) -> Mono<Status<U>>): Mono<Status<U>>
    fun orThen(operation: () -> Status<T>): Status<T>
    fun orThenMono(operation: () -> Mono<Status<T>>): Mono<Status<T>>
}

interface Success<T> : Status<T> {
    val value: T
}

@JsonSerialize(using = OkSerializer::class)
@JsonDeserialize(using = OkDeserializer::class)
data class Ok<T>(override val value: T) : Success<T> {
    override fun <U> andThen(next: (T) -> Status<U>) = next(value)

    override fun <U> andThenMono(next: (T) -> Mono<Status<U>>) = next(value)

    override fun <U> map(mapping: (T) -> U, or: () -> U) = mapping(value)

    override fun orThen(operation: () -> Status<T>) = this

    override fun orThenMono(operation: () -> Mono<Status<T>>): Mono<Status<T>> = Mono.just(this)

    override fun peek(onSuccess: (T) -> Any, onFailure: () -> Any): Status<T> {
        onSuccess(value)
        return this
    }
}

sealed class Error<T>(open val message: String) : Status<T> {

    override fun <U> andThen(next: (T) -> Status<U>) = orCastTo<U>()

    override fun <U> andThenMono(next: (T) -> Mono<Status<U>>) = Mono.just(orCastTo<U>())

    override fun <U> map(mapping: (T) -> U, or: () -> U) = or()

    abstract fun <U> orCastTo(): Status<U>

    override fun orThen(operation: () -> Status<T>) = operation()

    override fun orThenMono(operation: () -> Mono<Status<T>>) = operation()

    override fun peek(onSuccess: (T) -> Any, onFailure: () -> Any): Status<T> {
        onFailure()
        return this
    }

    abstract fun toException(): Exception
}

data class BadInput<T>(override val message: String) : Error<T>(message) {
    override fun <U> orCastTo() = BadInput<U>(message)

    override fun toException() = IllegalArgumentException(message)
}

data class BadRequest<T>(override val message: String) : Error<T>(message) {
    override fun <U> orCastTo() = BadRequest<U>(message)
    override fun toException() = IllegalStateException(message)
}

data class NotFound<T>(override val message: String) : Error<T>(message) {
    override fun <U> orCastTo() = NotFound<U>(message)
    override fun toException() = NoSuchElementException(message)
}

data class UnknownError<T>(val exception: Exception, override val message: String = exception.localizedMessage) :
    Error<T>(message) {
    override fun <U> orCastTo() = UnknownError<U>(exception)
    override fun toException() = exception
}

fun <I, O> Mono<Status<I>>.andThen(function: (I) -> Status<O>): Mono<Status<O>> {
    return this.map { status -> status.andThen(function) }
}

fun <I, O> Mono<Status<I>>.andThenMono(function: (I) -> Mono<Status<O>>): Mono<Status<O>> {
    return this.flatMap { status -> status.andThenMono(function) }
}
