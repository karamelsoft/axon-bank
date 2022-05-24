package org.karamelsoft.axon.demo.services.customers.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.karamelsoft.axon.demo.libraries.service.command.ClaimConstraint
import org.karamelsoft.axon.demo.libraries.service.command.ReleaseConstraint
import org.karamelsoft.axon.demo.libraries.service.command.ValidateConstraint
import org.karamelsoft.axon.demo.services.customers.api.*
import org.karamelsoft.research.axon.libraries.service.api.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CustomerService(val commandGateway: ReactorCommandGateway) {

    private val logger: Logger = LoggerFactory.getLogger(CustomerService::class.java)

    @CommandHandler
    fun registerUniqueCustomer(command: RegisterUniqueCustomer): Status<CustomerId> {
        logger.trace("received unique customer registration request")
        val customerConstraint = customerConstraint(
            command.details,
            command.address
        )

        fun claimConstraint() = commandGateway.send<Status<Unit>>(ClaimConstraint(customerConstraint))
        fun validateConstraint() = commandGateway.send<Status<Unit>>(ValidateConstraint(customerConstraint))
        fun releaseConstraint() = commandGateway.send<Status<Unit>>(ReleaseConstraint(customerConstraint))

        return claimConstraint()
            .flatMap(Status.andThenMono {
                commandGateway.send<Status<CustomerId>>(
                    RegisterNewCustomer(
                        customerId = command.customerId,
                        details = command.details,
                        address = command.address,
                        timestamp = command.timestamp
                    )
                )
            })
            .map(Status.peek(
                onSuccess = { validateConstraint() },
                onFailure = { releaseConstraint() }
            ))
            .doOnError { exception -> logger.error("error while creating customer", exception)}
            .block()!!
    }
}
