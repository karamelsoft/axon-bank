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

@Aggregate
internal class Card {

    @AggregateIdentifier
    private lateinit var cardId: CardId
    private var validity: CardValidity? = null
    private var pinCode: CardPinCode? = null
    private var blocked: Boolean = false
    private var wrongAttempts: Int = 0

    private fun notValidAnymore() = !(validity?.isAfter(Instant.now()) ?: false)

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun handle(command: RegisterNewCard) = Status.of {
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
    fun handle(command: BlockCard) =
        if (blocked) cardAlreadyBlocked()
        else Status.of {
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
    fun handle(command: SetupCardPinCode) = when {
        notValidAnymore() -> cardInvalid()
        blocked -> cardBlocked()
        pinCode != null -> cardPinCodeAlreadySetup()
        else -> Status.of {
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
    fun handle(command: ChangeCardPinCode) = when {
        notValidAnymore() -> cardInvalid()
        blocked -> cardBlocked()
        else -> Status.of {
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
    fun handle(command: ValidateCardPinCode) = when {
        notValidAnymore() -> cardInvalid()
        blocked -> cardBlocked()
        pinCode == null -> invalidCardPinCode()
        pinCode!!.validateCode(command.pinCode) -> Status.of {
            AggregateLifecycle.apply(PinCodeValidated(
                cardId = cardId,
                timestamp = command.timestamp
            ))
        }
        else -> Status.of {
            AggregateLifecycle.apply(PinCodeRejected(
                cardId = cardId,
                timestamp = command.timestamp
            ))
        }
    }

    @EventSourcingHandler
    fun on()
}