package org.karamelsoft.axon.demo.services.accounts.command

import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.karamelsoft.axon.demo.services.accounts.api.AmountDeposited
import org.karamelsoft.axon.demo.services.accounts.api.AmountWithdrew
import org.karamelsoft.axon.demo.services.accounts.api.DepositAmount
import org.karamelsoft.research.axon.libraries.service.api.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

@Saga
class TransferService {

    @Transient
    private val logger: Logger = LoggerFactory.getLogger(TransferService::class.java)

    @Autowired
    private lateinit var commandGateway: ReactorCommandGateway

    @StartSaga
    @SagaEventHandler(associationProperty = "operationId")
    fun on(event: AmountWithdrew) {
        logger.debug("transfer ${event.operationId} started")

        commandGateway.send<Status<Unit>>(DepositAmount(
            accountId = event.to,
            amount = event.amount,
            from = event.accountId,
            description = event.description,
            operationId = event.operationId
        )).block()
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "operationId")
    fun on(event: AmountDeposited) {
        logger.debug("transfer ${event.operationId} completed")
    }
}
