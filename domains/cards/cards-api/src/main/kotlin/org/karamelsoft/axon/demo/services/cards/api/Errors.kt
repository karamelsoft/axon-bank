package org.karamelsoft.axon.demo.services.cards.api

import org.karamelsoft.research.axon.libraries.service.api.BadInput
import org.karamelsoft.research.axon.libraries.service.api.BadRequest

fun <T> invalidCardNumber() = BadInput<T>("invalid card number")
fun <T> invalidCardValidity() = BadInput<T>("invalid card validity")
fun <T> undefinedCardPinCode() = BadInput<T>("undefined card pin code")
fun <T> wrongCardPinCode() = BadRequest<T>("wrong card pin code")
fun <T> cardPinCodeNotSetup() = BadRequest<T>("card pin code not setup")
fun <T> cardPinCodeAlreadySetup() = BadRequest<T>("card pin code already setup")
fun <T> cardInvalid() = BadRequest<T>("invalid card")
fun <T> cardBlocked() = BadRequest<T>("card blocked")
fun <T> cardAlreadyBlocked() = BadRequest<T>("card already blocked")
