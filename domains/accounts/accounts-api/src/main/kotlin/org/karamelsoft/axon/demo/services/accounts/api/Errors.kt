package org.karamelsoft.axon.demo.services.accounts.api

import org.karamelsoft.research.axon.libraries.service.api.BadInput
import org.karamelsoft.research.axon.libraries.service.api.BadRequest

fun <T> invalidAccountNumber() = BadInput<T>("invalid account number")
fun <T> accountClosed() = BadRequest<T>("account closed")
fun <T> notEnoughCredit() = BadRequest<T>("no enough credit")