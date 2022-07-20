package org.karamelsoft.axon.bank.services.cards.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import org.karamelsoft.axon.bank.services.cards.api.*
import org.karamelsoft.research.axon.libraries.artifacts.api.Status
import java.time.Instant
import java.time.LocalDate

const val MAX_WRONG_ATTEMPTS = 3

@Aggregate(snapshotTriggerDefinition = "cardSnapshotter")
internal class Card() {

    @AggregateIdentifier
    private lateinit var cardId: CardId
    private lateinit var owner: CardOwner
    private lateinit var account: CardAccount
    private lateinit var validity: CardValidity

    private var pinCode: CardPinCode? = null
    private var blocked: Boolean = false
    private var wrongAttempts: Int = 0

    private fun notValidAnymore() = validity.isBefore(LocalDate.now())
    private fun pinInvalid(pin: CardPinCode) = !(pinCode!!.validatePin(pin))

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun handle(command: CreateCard): Status<Unit> = Status.of {
        //TODO verify validity
        AggregateLifecycle.apply(
            CardCreated(
                cardId = command.cardId,
                validity = command.validity,
                account = command.account,
                owner = command.owner,
                timestamp = command.timestamp
            )
        )
    }

    @EventSourcingHandler
    fun on(event: CardCreated) {
        cardId = event.cardId
        owner = event.owner
        account = event.account
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
    fun handle(command: UseCard): Status<CardAssignments> = when {
        notValidAnymore() -> cardInvalid()
        blocked -> cardBlocked()
        pinCode == null -> undefinedCardPinCode()
        pinInvalid(command.pinCode) -> rejectPinCode(command.timestamp)
        else -> Status.of<CardAssignments> {
            AggregateLifecycle.apply(CardPinCodeValidated(
                cardId = cardId,
                timestamp = command.timestamp
            ))
            CardAssignments(
                owner = owner,
                account = account
            )
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

    fun <S> rejectPinCode(timestamp: Instant = Instant.now()): Status<S> {
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
