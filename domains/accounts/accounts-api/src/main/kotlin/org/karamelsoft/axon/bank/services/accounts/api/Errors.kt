package org.karamelsoft.axon.bank.services.accounts.api

import org.karamelsoft.research.axon.libraries.artifacts.api.BadInput
import org.karamelsoft.research.axon.libraries.artifacts.api.BadRequest

fun <T> invalidAccountNumber() = BadInput<T>("invalid account number")
fun <T> accountAlreadyExists() = BadRequest<T>("account already exists")
fun <T> accountClosed() = BadRequest<T>("account closed")
fun <T> notEnoughCredit() = BadRequest<T>("no enough credit")
fun <T> creditLineAlreadyOpened() = BadRequest<T>("credit line already opened")
fun <T> creditLineNotOpened() = BadRequest<T>("credit line not opened")
fun <T> creditLineNotReimbursed() = BadRequest<T>("credit line not reimbursed")
