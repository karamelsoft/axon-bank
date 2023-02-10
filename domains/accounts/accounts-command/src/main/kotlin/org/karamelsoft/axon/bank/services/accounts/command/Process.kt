package org.karamelsoft.axon.bank.services.accounts.command

import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.karamelsoft.axon.bank.services.accounts.api.AmountDeposited
import org.karamelsoft.axon.bank.services.accounts.api.AmountWithdrawn
import org.karamelsoft.axon.bank.services.accounts.api.DepositAmount
import org.karamelsoft.research.axon.libraries.artifacts.api.Status
import org.springframework.beans.factory.annotation.Autowired

@Saga
class TransferProcess {

    @Autowired
    private lateinit var commandGateway: ReactorCommandGateway

    @StartSaga
    @SagaEventHandler(associationProperty = "operationId")
    fun on(event: AmountWithdrawn) {
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
    fun on(event: AmountDeposited) {}
}
