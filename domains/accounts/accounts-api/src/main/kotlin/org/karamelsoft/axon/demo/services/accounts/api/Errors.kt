package org.karamelsoft.axon.demo.services.accounts.api

import org.karamelsoft.research.axon.libraries.artifacts.api.BadInput
import org.karamelsoft.research.axon.libraries.artifacts.api.BadRequest

fun <T> invalidAccountNumber() = BadInput<T>("invalid account number")
fun <T> accountAlreadyExists() = BadRequest<T>("account already exists")
fun <T> accountClosed() = BadRequest<T>("account closed")
fun <T> notEnoughCredit() = BadRequest<T>("no enough credit")
