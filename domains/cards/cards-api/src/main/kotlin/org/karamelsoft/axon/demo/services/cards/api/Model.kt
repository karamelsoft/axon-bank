package org.karamelsoft.axon.demo.services.cards.api

import com.google.common.hash.Hashing
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

private val cardIdPattern: Pattern = Pattern.compile("\\d{16}")
private val pinCodePattern: Pattern = Pattern.compile("\\d{4,6}")

data class CardId(val number: String = generate()) {
    init {
        if (!cardIdPattern.matcher(number).matches()) {
            throw invalidCardNumber<Unit>().toException()
        }
    }
}

fun generate(): String {
    return "${(1000_0000_0000_0000..9999_9999_9999_9999).random(kotlin.random.Random(System.nanoTime()))}"
}

data class CardAccount(val reference: String)
data class CardOwner(val reference: String)

data class CardAssignments(
    val owner: CardOwner,
    val account: CardAccount
)

fun currentMonth(): Month = LocalDate.now().month
fun currentYear(): Year = Year.now()

data class CardValidity(val month: Month = currentMonth(), val year: Year = currentYear().plus(5, ChronoUnit.YEARS)) {
    init {
        if (year.isBefore(Year.now())) {
            throw invalidCardValidity<Unit>().toException()
        }
    }

    fun isBefore(date: LocalDate) = when {
        date.year > year.value -> true
        date.year == year.value && date.month.value > month.value -> true
        else -> false
    }
}

private fun hashingFunction() = Hashing.sha256()
private fun hash(code: String) = hashingFunction().newHasher().putBytes(code.toByteArray()).hash().asLong()

data class CardPinCode(val hash: Long) {
    companion object {
        fun of(code: String): CardPinCode {
            if (!pinCodePattern.matcher(code).matches()) {
                throw undefinedCardPinCode<Unit>().toException()
            }

            return CardPinCode(hash(code))
        }
    }

    fun validatePin(code: CardPinCode) = (hash == code.hash)
}
