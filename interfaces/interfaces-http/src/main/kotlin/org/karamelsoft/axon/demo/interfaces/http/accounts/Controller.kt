package org.karamelsoft.axon.demo.interfaces.http.accounts

import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.karamelsoft.axon.demo.interfaces.http.Event
import org.karamelsoft.axon.demo.services.accounts.api.AccountId
import org.karamelsoft.axon.demo.services.accounts.api.DepositAmount
import org.karamelsoft.axon.demo.services.accounts.api.RegisterNewAccount
import org.karamelsoft.axon.demo.services.accounts.api.WithdrawAmount
import org.karamelsoft.research.axon.libraries.service.api.Status
import org.karamelsoft.research.axon.libraries.service.module.readEvents
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val commandGateway: ReactorCommandGateway,
    private val eventStore: EventStore,
) {

    @PutMapping("/{accountId}")
    fun registerAccount(@PathVariable accountId: String, @RequestBody action: AccountRegistration): Mono<Status<Unit>> {
        return commandGateway.send(
            RegisterNewAccount(
                accountId = AccountId(accountId),
                owner = action.owner,
                timestamp = action.timestamp
            )
        )
    }

    @PostMapping("/{accountId}/deposit")
    fun depositAmount(@PathVariable accountId: String, @RequestBody action: AmountDeposit): Mono<Status<Unit>> {
        return commandGateway.send(
            DepositAmount(
                accountId = AccountId(accountId),
                amount = action.amount,
                from = action.from,
                description = action.description,
                timestamp = action.timestamp
            )
        )
    }

    @PostMapping("/{accountId}/withdraw")
    fun withdrawAmount(@PathVariable accountId: String, @RequestBody action: AmountWithdrawal): Mono<Status<Unit>> {
        return commandGateway.send(
            WithdrawAmount(
                accountId = AccountId(accountId),
                amount = action.amount,
                to = action.to,
                description = action.description,
                timestamp = action.timestamp
            )
        )
    }

    @GetMapping("/{accountId}/history")
    fun getAccountHistory(@PathVariable("accountId") accountId: String): Flux<AccountFrame> {
        return eventStore.readEvents(AccountId(accountId))
            .map { event -> event.payload }
            .map { payload -> AccountFrame.from(payload) }
            .scan(AccountFrame::aggregate)
    }
}
