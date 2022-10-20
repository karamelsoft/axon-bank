package org.karamelsoft.axon.bank.services.credits.api

import java.util.*

data class CreditLineId(val value: UUID = UUID.randomUUID()) {
    companion object {
        fun of(value: String) = CreditLineId(UUID.fromString(value))
    }

    fun asReference() = value.toString()
}
