package org.karamelsoft.axon.demo.services.cards.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import org.karamelsoft.axon.demo.services.cards.api.*
import org.karamelsoft.research.axon.libraries.service.api.Status
import java.time.Instant
import java.time.LocalDate

const val MAX_WRONG_ATTEMPTS = 3

@Aggregate
internal class Card {

    @AggregateIdentifier
    private lateinit var cardId: CardId
    private var validity: CardValidity? = null
    private var pinCode: CardPinCode? = null
    private var blocked: Boolean = false
    private var wrongAttempts: Int = 0

    private fun notValidAnymore() = validity!!.isBefore(LocalDate.now())
    private fun pinInvalid(pin: CardPinCode) = !(pinCode!!.validatePin(pin))

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun handle(command: RegisterNewCard): Status<Unit> = Status.of {
        AggregateLifecycle.apply(
            NewCardRegistered(
                cardId = command.cardId,
                validity = command.validity,
                account = command.account,
                owner = command.owner,
                timestamp = command.timestamp
            )
        )
    }

    @EventSourcingHandler
    fun on(event: NewCardRegistered) {
        cardId = event.cardId
        validity = event.validity
    }

    @CommandHandler
    fun handle(command: BlockCard): Status<Unit> =
        if (blocked) cardAlreadyBlocked()
        else Status.of<Unit> {
            AggregateLifecycle.apply(
                CardBlocked(
                    cardId = cardId,
                    timestamp = command.timestamp
                )
            )
        }

    @EventSourcingHandler
    fun on(event: CardBlocked) {
        blocked = true
    }

    @CommandHandler
    fun handle(command: SetupCardPinCode): Status<Unit> = when {
        notValidAnymore() -> cardInvalid()
        blocked -> cardBlocked()
        pinCode != null -> cardPinCodeAlreadySetup()
        else -> Status.of<Unit> {
            AggregateLifecycle.apply(
                CardPinCodeSetup(
                    cardId = cardId,
                    pinCode = command.pinCode,
                    timestamp = command.timestamp
                )
            )
        }
    }

    @EventSourcingHandler
    fun on(event: CardPinCodeSetup) {
        pinCode = event.pinCode
    }

    @CommandHandler
    fun handle(command: ChangeCardPinCode): Status<Unit> = when {
        notValidAnymore() -> cardInvalid()
        blocked -> cardBlocked()
        pinCode == null -> undefinedCardPinCode()
        pinInvalid(command.currentPinCode) -> rejectPinCode(command.timestamp)
        else -> Status.of<Unit> {
            AggregateLifecycle.apply(
                CardPinCodeChanged(
                    cardId = cardId,
                    newPinCode = command.newPinCode,
                    timestamp = command.timestamp
                )
            )
        }
    }

    @EventSourcingHandler
    fun on(event: CardPinCodeChanged) {
        pinCode = event.newPinCode
    }

    @CommandHandler
    fun handle(command: ValidateCardPinCode): Status<Unit> = when {
        notValidAnymore() -> cardInvalid()
        blocked -> cardBlocked()
        pinCode == null -> undefinedCardPinCode()
        pinInvalid(command.pinCode) -> rejectPinCode(command.timestamp)
        else -> Status.of<Unit> {
            AggregateLifecycle.apply(CardPinCodeValidated(
                cardId = cardId,
                timestamp = command.timestamp
            ))
        }
    }

    @EventSourcingHandler
    fun on(event: CardPinCodeValidationFailed) {
        wrongAttempts++
    }

    @EventSourcingHandler
    fun on(event: CardPinCodeValidated) {
        wrongAttempts = 0
    }

    fun rejectPinCode(timestamp: Instant = Instant.now()): Status<Unit> {
        AggregateLifecycle.apply(CardPinCodeValidationFailed(
            cardId = cardId,
            timestamp = timestamp
        ))
        if (wrongAttempts >= MAX_WRONG_ATTEMPTS) {
            AggregateLifecycle.apply(CardBlocked(
                cardId = cardId,
                timestamp = timestamp
            ))
        }
        return wrongCardPinCode()
    }
}
