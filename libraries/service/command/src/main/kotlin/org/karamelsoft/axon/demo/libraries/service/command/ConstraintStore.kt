package org.karamelsoft.axon.demo.libraries.service.command

import org.karamelsoft.research.axon.libraries.service.api.Status
import java.time.Duration

interface ConstraintStore {
    fun claimConstraint(id: String): Status<Unit>
    fun validateConstraint(id: String): Status<Unit>
    fun releaseConstraint(id: String): Status<Unit>

    fun <S> claim(id: String, operation: () -> Status<S>): Status<S> {
        return claimConstraint(id).andThen {
            operation().peek(
                validate<S>(id),
                release(id)
            )
        }
    }

    fun <V> validate(id: String): (V) -> Unit = {
        validateConstraint(id)
    }

    fun release(id: String): () -> Unit = {
        releaseConstraint(id)
    }
}
