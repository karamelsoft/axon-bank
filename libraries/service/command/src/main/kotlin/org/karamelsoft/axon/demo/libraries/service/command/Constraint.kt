package org.karamelsoft.axon.demo.libraries.service.command

import org.karamelsoft.research.axon.libraries.service.api.BadRequest
import org.karamelsoft.research.axon.libraries.service.api.Status

fun ok(): Status<Unit> = Status.ok {  }
fun alreadyClaimed(): Status<Unit> = BadRequest("constraint already claimed")
fun alreadyValidated(): Status<Unit> = BadRequest("constraint already validated")

sealed interface Constraint {
    fun claim(): Status<Unit>
    fun claimed(): Constraint = Claimed
    fun validate(): Status<Unit>
    fun validated(): Constraint = Validated
    fun release(): Status<Unit>
    fun released(): Constraint = Available
}

object Available : Constraint {
    override fun claim() = ok()
    override fun validate() = ok()
    override fun release() = ok()
}

object Claimed : Constraint {
    override fun claim() = alreadyClaimed()
    override fun validate() = ok()
    override fun release() = ok()
}

object Validated : Constraint {
    override fun claim() = alreadyValidated()
    override fun validate() = alreadyValidated()
    override fun release() = ok()
}
