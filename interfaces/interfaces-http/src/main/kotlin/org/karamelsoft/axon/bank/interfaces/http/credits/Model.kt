package org.karamelsoft.axon.bank.interfaces.http.credits

data class CreditLineResponse(val id: String, val threshold: Double)

data class CreditLineBody(val amount: Double)