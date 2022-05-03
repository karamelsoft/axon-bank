package org.karamelsoft.axon.demo.orchestrators.customer.registration

import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.karamelsoft.axon.demo.services.accounts.api.AccountId
import org.karamelsoft.axon.demo.services.accounts.api.AccountOwner
import org.karamelsoft.axon.demo.services.accounts.api.NewAccountRegistered
import org.karamelsoft.axon.demo.services.accounts.api.RegisterNewAccount
import org.karamelsoft.axon.demo.services.customers.api.CustomerId
import org.karamelsoft.axon.demo.services.customers.api.NewCustomerRegistered
import org.karamelsoft.research.axon.libraries.service.api.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

@Saga
class CustomerRegistrationProcess {

    @Transient
    private val logger: Logger = LoggerFactory.getLogger(CustomerRegistrationProcess::class.java)

    @Autowired
    private lateinit var commandGateway: ReactorCommandGateway

    private lateinit var customerId: CustomerId
    private lateinit var accountId: AccountId

    @StartSaga
    @SagaEventHandler(associationProperty = "customerId")
    fun handle(event: NewCustomerRegistered) {
        logger.info("New customer registration should create an account")
        customerId = event.customerId
        accountId = AccountId()
        SagaLifecycle.associateWith("accountId", accountId.toString())
        commandGateway.send<Status<Unit>>(RegisterNewAccount(
            accountId = accountId,
            owner = AccountOwner(customerId.asReference())
        )).block()
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "accountId")
    fun handle(event: NewAccountRegistered) {
        logger.info("Account created")
    }
}
