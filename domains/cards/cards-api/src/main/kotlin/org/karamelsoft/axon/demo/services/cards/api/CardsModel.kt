package org.karamelsoft.axon.demo.services.cards.api

import com.google.common.hash.Hashing
import org.axonframework.modelling.command.AggregateLifecycle
import org.karamelsoft.research.axon.libraries.service.api.Status
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.util.regex.Pattern

private val cardIdPattern: Pattern = Pattern.compile("\\d{16}")
private val pinCodePattern: Pattern = Pattern.compile("\\d{4,6}")
private val securityCodePattern: Pattern = Pattern.compile("\\d{3}")

data class CardId(val number: String) {
    companion object {
        fun of(id: String): CardId {
            if (!cardIdPattern.matcher(id).matches()) {
                throw invalidCardNumber<Unit>().toException()
            }

            return CardId(id)
        }
    }
}

data class CardAccount(val reference: String)
data class CardOwner(val reference: String)

data class CardValidity(val month: Month, val year: Year) {
    companion object {
        fun of(month: Month, year: Year): CardValidity {
            if (year.isBefore(Year.now())) {
                throw invalidCardValidity<Unit>().toException()
            }

            return CardValidity(month, year)
        }
    }

    fun isAfter(timestamp: Instant): Boolean {
        val date = LocalDate.from(timestamp)
        val dateYear = date.year
        val dateMonth = date.month

        return year.value >= dateYear && month.value >= dateMonth.value
    }
}

private fun hashingFunction() = Hashing.sha256()
private fun hash(code: String) = hashingFunction().newHasher().putBytes(code.toByteArray()).hash().asLong()

data class CardPinCode(private val hash: Long) {
    companion object {
        fun of(code: String): CardPinCode {
            if (!pinCodePattern.matcher(code).matches()) {
                throw invalidCardPinCode<Unit>().toException()
            }

            return CardPinCode(hash(code))
        }
    }

    fun validateCode(code: CardPinCode) = (hash == code.hash)
}
