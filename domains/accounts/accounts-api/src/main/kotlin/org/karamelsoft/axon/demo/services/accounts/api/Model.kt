package org.karamelsoft.axon.demo.services.accounts.api

import java.util.UUID
import java.util.regex.Pattern

private val AccountIdPattern = Pattern.compile("([A-Z]{2})(\\d{2})(\\d{12,26})")

data class AccountId(val number: String = generate()) {
    init {
        if (!AccountIdPattern.matcher(number).matches()) {
            throw invalidAccountNumber<String>().toException()
        }
    }
}

fun generate(): String {
    return "BE${(10_0000_0000_0000..99_9999_9999_9999).random(kotlin.random.Random(System.nanoTime()))}"
}

data class AccountOwner(val reference: String) {
    companion object {
        fun from(uuid: UUID) = AccountOwner(uuid.toString())
    }
}
