package org.karamelsoft.research.axon.libraries.service.api

sealed interface Status<T> {
    companion object {
        fun <T> of(operation: () -> T) = try {
            Ok(operation())
        } catch (e: Exception) {
            UnknownError(e)
        }
    }
    fun <U> map(mapping: (T) ->  U, or: () -> U) : U
    fun <U> andThen(next: (T) -> Status<U>): Status<U>

    fun orThen(operation: () -> Status<T>): Status<T>
}

interface Success<T>: Status<T> {
    val value: T
}

data class Ok<T>(override val value: T): Success<T> {
    override fun <U> andThen(next: (T) -> Status<U>) = next(value)
    override fun <U> map(mapping: (T) -> U, or: () -> U) = mapping(value)
    override fun orThen(operation: () -> Status<T>) = this
}

sealed class Error<T>(open val message: String): Status<T> {
    override fun <U> andThen(next: (T) -> Status<U>) = orCastTo<U>()
    override fun <U> map(mapping: (T) -> U, or: () -> U) = or()
    abstract fun <U> orCastTo(): Status<U>
    override fun orThen(operation: () -> Status<T>) = operation()
    abstract fun toException() : Exception
}

data class BadInput<T>(override val message: String): Error<T>(message) {
    override fun <U> orCastTo() = BadInput<U>(message)

    override fun toException() = IllegalArgumentException(message)
}

data class BadRequest<T>(override val message: String): Error<T>(message) {
    override fun <U> orCastTo() = BadRequest<U>(message)
    override fun toException() = IllegalStateException(message)
}

data class NotFound<T>(override val message: String): Error<T>(message) {
    override fun <U> orCastTo() = NotFound<U>(message)
    override fun toException() = NoSuchElementException(message)
}
data class UnknownError<T>(val exception: Exception, override val message: String = exception.localizedMessage): Error<T>(message) {
    override fun <U> orCastTo() = UnknownError<U>(exception)
    override fun toException() = exception
}
