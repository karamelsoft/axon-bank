package org.karamelsoft.axon.demo.interfaces.http.customers

import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway
import org.karamelsoft.axon.demo.interfaces.http.handleStatus
import org.karamelsoft.axon.demo.services.customers.api.CorrectCustomerAddress
import org.karamelsoft.axon.demo.services.customers.api.CorrectCustomerDetails
import org.karamelsoft.axon.demo.services.customers.api.CustomerId
import org.karamelsoft.axon.demo.services.customers.api.RegisterUniqueCustomer
import org.karamelsoft.axon.demo.views.customer.dashboard.api.CustomerDashboardResponse
import org.karamelsoft.axon.demo.views.customer.dashboard.api.GetCustomerDashboard
import org.karamelsoft.research.axon.libraries.artifacts.api.Status
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/customers")
class CustomerController(
    val commandGateway: ReactorCommandGateway,
    val queryGateway: ReactorQueryGateway
) {

    @PostMapping()
    fun registerCustomerWithoutId(@RequestBody action: CustomerRegistration): Mono<CustomerId> {
        return commandGateway.send<Status<CustomerId>>(
            RegisterUniqueCustomer(
                customerId = CustomerId(),
                details = action.details,
                address = action.address,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PutMapping("/{customerId}")
    fun registerCustomer(
        @PathVariable("customerId") customerId: String,
        @RequestBody action: CustomerRegistration
    ): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            RegisterUniqueCustomer(
                customerId = CustomerId.of(customerId),
                details = action.details,
                address = action.address,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PutMapping("/{customerId}/details")
    fun correctCustomerAddress(
        @PathVariable("customerId") customerId: String,
        @RequestBody action: CustomerDetailsCorrection
    ): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            CorrectCustomerDetails(
                customerId = CustomerId.of(customerId),
                newDetails = action.newDetails,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @PutMapping("/{customerId}/address")
    fun correctCustomerAddress(
        @PathVariable("customerId") customerId: String,
        @RequestBody action: CustomerAddressCorrection
    ): Mono<Unit> {
        return commandGateway.send<Status<Unit>>(
            CorrectCustomerAddress(
                customerId = CustomerId.of(customerId),
                newAddress = action.newAddress,
                timestamp = action.timestamp
            )
        ).handleStatus()
    }

    @GetMapping("/{customerId}/dashboard")
    fun getCustomerDashboard(@PathVariable("customerId") customerId: String): Mono<CustomerDashboardResponse> {
        return queryGateway.query(
            GetCustomerDashboard(org.karamelsoft.axon.demo.views.customer.dashboard.api.CustomerId.from(customerId)),
            CustomerDashboardResponse::class.java
        )
    }

}
