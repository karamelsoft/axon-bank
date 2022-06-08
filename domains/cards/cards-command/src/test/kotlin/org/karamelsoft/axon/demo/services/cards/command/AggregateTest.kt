package org.karamelsoft.axon.demo.services.cards.command

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.karamelsoft.axon.demo.libraries.service.command.test.AggregateTest
import org.karamelsoft.axon.demo.libraries.service.test.serialize
import org.karamelsoft.axon.demo.services.cards.api.*
import org.karamelsoft.research.axon.libraries.service.api.BadRequest
import org.karamelsoft.research.axon.libraries.service.api.Status
import java.time.Instant
import java.time.temporal.ChronoUnit

internal class CardTest : AggregateTest<Card>(Card::class.java) {

    private val cardId = CardId()
    private val validity = CardValidity()
    private val pinCode = CardPinCode.of("1234")
    private val newPinCode = CardPinCode.of("2341")
    private val wrongPinCode = CardPinCode.of("3412")
    private val account = CardAccount("fake account")
    private val owner = CardOwner("fake owner")
    private val now = Instant.now()

    @Test
    fun `registering a new card`() {
        aggregate
            .given()
            .`when`(serialize(RegisterNewCard(cardId, validity, account, owner, now)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(serialize( NewCardRegistered(cardId, validity, account, owner, now)))
    }

    @Test
    fun `block a card`() {
        aggregate
            .given(serialize( NewCardRegistered(cardId, validity, account, owner, now)))
            .`when`(serialize( BlockCard(cardId, now.plus(5, ChronoUnit.DAYS))))
            .expectSuccessfulHandlerExecution()
            .expectEvents(serialize( CardBlocked(cardId, now.plus(5, ChronoUnit.DAYS))))
    }

    @Test
    fun `block a card twice`() {
        aggregate
            .given(serialize( NewCardRegistered(cardId, validity, account, owner, now)))
            .andGiven(serialize( CardBlocked(cardId, now.plus(5, ChronoUnit.DAYS))))
            .`when`(serialize( BlockCard(cardId, now.plus(6, ChronoUnit.DAYS))))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
            .expectResultMessagePayload(serialize( BadRequest<Unit>("card already blocked")))
    }

    @Test
    fun `setup pin code`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .`when`(SetupCardPinCode(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
    }

    @Test
    fun `setup pin code twice`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .`when`(SetupCardPinCode(cardId, pinCode, now.plus(2, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
            .expectResultMessagePayload(BadRequest<Unit>("card pin code already setup"))
    }

    @Test
    fun `setup pin code after card blocked`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardBlocked(cardId, now.plus(1, ChronoUnit.DAYS)))
            .`when`(SetupCardPinCode(cardId, pinCode, now.plus(2, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
            .expectResultMessagePayload(BadRequest<Unit>("card blocked"))
    }

    @Test
    fun `change pin code`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .`when`(ChangeCardPinCode(cardId, pinCode, newPinCode, now.plus(2, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(CardPinCodeChanged(cardId, newPinCode, now.plus(2, ChronoUnit.DAYS)))
    }

    @Test
    fun `change pin code without pin setup`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .`when`(ChangeCardPinCode(cardId, pinCode, newPinCode, now.plus(2, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
            .expectResultMessagePayload(BadRequest<Unit>("undefined card pin code"))
    }

    @Test
    fun `change pin code again`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .andGiven(CardPinCodeChanged(cardId, newPinCode, now.plus(2, ChronoUnit.DAYS)))
            .`when`(ChangeCardPinCode(cardId, newPinCode, pinCode, now.plus(3, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(CardPinCodeChanged(cardId, pinCode, now.plus(3, ChronoUnit.DAYS)))
    }

    @Test
    fun `change pin code after card blocked`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .andGiven(CardBlocked(cardId, now.plus(2, ChronoUnit.DAYS)))
            .`when`(ChangeCardPinCode(cardId, newPinCode, pinCode, now.plus(3, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
            .expectResultMessagePayload(BadRequest<Unit>("card blocked"))
    }

    @Test
    fun `use card`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .andGiven(CardPinCodeChanged(cardId, newPinCode, now.plus(2, ChronoUnit.DAYS)))
            .`when`(UseCard(cardId, newPinCode, now.plus(3, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectResultMessagePayload(Status.of { CardAssignments(owner, account) })
            .expectEvents(CardPinCodeValidated(cardId, now.plus(3, ChronoUnit.DAYS)))
    }

    @Test
    fun `use card with wrong pin code`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .`when`(UseCard(cardId, wrongPinCode, now.plus(3, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(CardPinCodeValidationFailed(cardId, now.plus(3, ChronoUnit.DAYS)))
            .expectResultMessagePayload(BadRequest<CardAssignments>("wrong card pin code"))
    }

    @Test
    fun `use card with good pin code after 2 failures`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .andGiven(CardPinCodeValidationFailed(cardId, now.plus(3, ChronoUnit.DAYS)))
            .andGiven(CardPinCodeValidationFailed(cardId, now.plus(3, ChronoUnit.DAYS)))
            .`when`(UseCard(cardId, pinCode, now.plus(3, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectResultMessagePayload(Status.of { CardAssignments(owner, account) })
            .expectEvents(CardPinCodeValidated(cardId, now.plus(3, ChronoUnit.DAYS)))
    }

    @Test
    fun `use card with wrong pin code after 2 failures and 1 success`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .andGiven(CardPinCodeValidationFailed(cardId, now.plus(3, ChronoUnit.DAYS)))
            .andGiven(CardPinCodeValidationFailed(cardId, now.plus(3, ChronoUnit.DAYS)))
            .andGiven(CardPinCodeValidated(cardId, now.plus(3, ChronoUnit.DAYS)))
            .`when`(UseCard(cardId, wrongPinCode, now.plus(3, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(CardPinCodeValidationFailed(cardId, now.plus(3, ChronoUnit.DAYS)))
            .expectResultMessagePayload(BadRequest<CardAssignments>("wrong card pin code"))
    }

    @Test
    fun `use card with wrong pin code after 2 failures`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardPinCodeSetup(cardId, pinCode, now.plus(1, ChronoUnit.DAYS)))
            .andGiven(CardPinCodeValidationFailed(cardId, now.plus(3, ChronoUnit.DAYS)))
            .andGiven(CardPinCodeValidationFailed(cardId, now.plus(3, ChronoUnit.DAYS)))
            .`when`(UseCard(cardId, wrongPinCode, now.plus(3, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                CardPinCodeValidationFailed(cardId, now.plus(3, ChronoUnit.DAYS)),
                CardBlocked(cardId, now.plus(3, ChronoUnit.DAYS))
            )
            .expectResultMessagePayload(BadRequest<CardAssignments>("wrong card pin code"))
    }

    @Test
    fun `use card after card blocked`() {
        aggregate
            .given(NewCardRegistered(cardId, validity, account, owner, now))
            .andGiven(CardBlocked(cardId, now.plus(3, ChronoUnit.DAYS)))
            .`when`(UseCard(cardId, wrongPinCode, now.plus(3, ChronoUnit.DAYS)))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
            .expectResultMessagePayload(BadRequest<CardAssignments>("card blocked"))
    }
}
