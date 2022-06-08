package org.karamelsoft.axon.demo.services.customers.command

import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
internal class CustomerConstraint {

    @AggregateIdentifier
    private var id: Long = 0

}
