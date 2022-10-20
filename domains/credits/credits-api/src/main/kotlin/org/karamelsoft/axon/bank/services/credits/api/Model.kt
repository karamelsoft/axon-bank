package org.karamelsoft.axon.bank.services.credits.api

import java.time.Duration
import java.time.LocalDateTime
import java.time.Period
import java.util.*

data class CreditLineId(val value: UUID = UUID.randomUUID()) {
    companion object {
        fun from(value: String) = CreditLineId(UUID.fromString(value))
    }
}

data class CreditorAccount(val reference: String)

data class CreditLineValidity(val openingDate: LocalDateTime, val period: Period) {
    init {
        if (period.isZero || period.isNegative) {
            throw IllegalArgumentException("credit line duration must be positive")
        }
    }

    fun isActive() = isActive(LocalDateTime.now())

    fun isActive(date: LocalDateTime) = date.isAfter(openingDate) && date.isBefore(openingDate.plus(period))
}
