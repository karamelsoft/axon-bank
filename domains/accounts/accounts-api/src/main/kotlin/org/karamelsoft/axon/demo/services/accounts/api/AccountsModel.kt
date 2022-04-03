package org.karamelsoft.axon.demo.services.accounts.api

import java.util.regex.Pattern

private val AccountIdPattern = Pattern.compile("([A-Z]{2})(\\d{2})(\\d{12,26})")

data class AccountId(val number: String) {
    companion object {
        fun create(number: String): AccountId {
            if(!AccountIdPattern.matcher(number).matches()) {
                throw invalidAccountNumber<String>().toException()
            }

            return AccountId(number)
        }
    }
}

data class AccountOwner(val reference: String)