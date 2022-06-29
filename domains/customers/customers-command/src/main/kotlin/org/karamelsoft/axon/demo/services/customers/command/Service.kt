package org.karamelsoft.axon.demo.services.customers.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.karamelsoft.axon.demo.services.customers.api.CustomerId
import org.karamelsoft.axon.demo.services.customers.api.RegisterNewCustomer
import org.karamelsoft.axon.demo.services.customers.api.RegisterUniqueCustomer
import org.karamelsoft.research.axon.libraries.artifacts.api.Status
import org.springframework.stereotype.Component

@Component
class CustomerService(val commandGateway: ReactorCommandGateway, val constraintStore: CustomerConstraintStore) {
    @CommandHandler
    fun registerUniqueCustomer(command: RegisterUniqueCustomer): Status<CustomerId> {
        return constraintStore.create(CustomerHash.from(command.details)) {
            commandGateway.send<Status<CustomerId>>(
                RegisterNewCustomer(
                    customerId = command.customerId,
                    details = command.details,
                    address = command.address,
                    timestamp = command.timestamp
                )
            ).block()!!
        }
    }
}
