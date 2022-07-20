package org.karamelsoft.axon.bank.services.customers.command

import com.google.common.hash.Hashing
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.axonframework.modelling.command.*
import org.axonframework.spring.stereotype.Aggregate
import org.karamelsoft.axon.bank.services.customers.api.CustomerDetails
import org.karamelsoft.research.axon.libraries.artifacts.api.BadRequest
import org.karamelsoft.research.axon.libraries.artifacts.api.Status
import org.karamelsoft.research.axon.libraries.artifacts.api.andThenMono
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.format.DateTimeFormatter

interface CustomerConstraintStore {
    fun <S> create(hash: CustomerHash, operation: () -> Status<S>): Status<S>
    fun <S> update(previousHash: CustomerHash, newHash: CustomerHash, operation: () -> Status<S>): Status<S>

}

data class CustomerHash(val value: Long) {
    companion object {
        private val hashFunction = Hashing.sha256()
        private val charset = Charsets.UTF_8

        fun from(details: CustomerDetails): CustomerHash {
            return CustomerHash(
                hashFunction.newHasher()
                    .putString(details.firstName, charset)
                    .putString(details.lastName, charset)
                    .putString(details.birthDate.format(DateTimeFormatter.ISO_DATE), charset)
                    .hash().asLong()
            )
        }
    }
}

@Service
class AggregateCustomerConstraintStore(val commandGateway: ReactorCommandGateway) : CustomerConstraintStore {

    override fun <S> create(hash: CustomerHash, operation: () -> Status<S>): Status<S> {
        return claimConstraint(hash)
            .andThenMono {
                val status = operation()
                status.orThenMono { releaseConstraint(hash).andThenMono { Mono.just(status) } }
            }
            .block()!!
    }

    override fun <S> update(previousHash: CustomerHash, newHash: CustomerHash, operation: () -> Status<S>): Status<S> {
        return claimConstraint(newHash)
            .andThenMono {
                val status = operation()
                status
                    .orThenMono { releaseConstraint(newHash).andThenMono { Mono.just(status) } }
                    .andThenMono { releaseConstraint(previousHash).andThenMono { Mono.just(status) } }
            }
            .block()!!
    }

    private fun claimConstraint(hash: CustomerHash): Mono<Status<Unit>> = commandGateway.send(ClaimCustomerConstraint(hash))
    private fun releaseConstraint(hash: CustomerHash): Mono<Status<Unit>> = commandGateway.send(ReleaseCustomerConstraint(hash))
}

fun customerAlreadyExists() = BadRequest<Unit>(message = "customer already exists")

data class ClaimCustomerConstraint(@TargetAggregateIdentifier val customerHash: CustomerHash)
data class ReleaseCustomerConstraint(@TargetAggregateIdentifier val customerHash: CustomerHash)
data class CustomerConstraintClaimed(val customerHash: CustomerHash)
data class CustomerConstraintReleased(val customerHash: CustomerHash)


@Aggregate
internal class CustomerConstraint() {

    @AggregateIdentifier
    private lateinit var hash: CustomerHash
    private var claimed = false

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun handle(command: ClaimCustomerConstraint): Status<Unit> =
        if (claimed) customerAlreadyExists()
        else Status.of<Unit> {
            AggregateLifecycle.apply(CustomerConstraintClaimed(command.customerHash))
        }


    @CommandHandler
    fun handle(command: ReleaseCustomerConstraint): Status<Unit> = Status.of<Unit> {
        AggregateLifecycle.apply(CustomerConstraintReleased(command.customerHash))
    }

    @EventSourcingHandler
    fun on(event: CustomerConstraintClaimed) {
        hash = event.customerHash
        claimed = true
    }

    @EventSourcingHandler
    fun on(event: CustomerConstraintReleased) {
        claimed = false
    }
}
