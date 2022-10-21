package org.karamelsoft.axon.bank.interfaces.http.accounts

import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.karamelsoft.axon.bank.interfaces.http.handleStatus
import org.karamelsoft.axon.bank.services.accounts.api.*
import org.karamelsoft.research.axon.libraries.artifacts.api.Status
import org.karamelsoft.research.axon.libraries.artifacts.module.readEvents
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val commandGateway: ReactorCommandGateway,
    private val eventStore: EventStore,
) {

    @PostMapping("/{accountId}/deposit")
    fun depositAmount(@PathVariable accountId: String, @RequestBody action: AmountDeposit): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            DepositAmount(
                accountId = AccountId(accountId),
                amount = action.amount,
                from = AccountId(action.from),
                description = action.description,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PostMapping("/{accountId}/withdraw")
    fun withdrawAmount(@PathVariable accountId: String, @RequestBody action: AmountWithdrawal): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            WithdrawAmount(
                accountId = AccountId(accountId),
                amount = action.amount,
                to = AccountId(action.to),
                description = action.description,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PostMapping("/{accountId}/creditLine")
    fun openCreditLine(@PathVariable accountId: String, @RequestBody action: CreditLineOpening): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            OpenCreditLine(
                accountId = AccountId(accountId),
                amount = action.amount,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @DeleteMapping("/{accountId}/creditLine")
    fun closeCreditLine(@PathVariable accountId: String, @RequestBody action: CreditLineClosing): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            CloseCreditLine(
                accountId = AccountId(accountId),
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PostMapping("/{accountId}/creditLine/increase")
    fun increaseCreditLine(@PathVariable accountId: String, @RequestBody action: CreditLineIncrease): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            IncreaseCreditLine(
                accountId = AccountId(accountId),
                amount = action.amount,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PostMapping("/{accountId}/creditLine/decrease")
    fun decreaseCreditLine(@PathVariable accountId: String, @RequestBody action: CreditLineDecrease): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            DecreaseCreditLine(
                accountId = AccountId(accountId),
                amount = action.amount,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @GetMapping("/{accountId}/history")
    fun getAccountHistory(@PathVariable("accountId") accountId: String): Flux<AccountFrame> {
        return eventStore.readEvents(AccountId(accountId))
            .map { event -> event.payload }
            .map { payload -> AccountFrame.from(payload) }
            .scan(AccountFrame::aggregate)
    }
}
