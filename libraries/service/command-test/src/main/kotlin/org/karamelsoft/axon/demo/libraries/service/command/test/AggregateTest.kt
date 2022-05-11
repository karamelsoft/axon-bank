package org.karamelsoft.axon.demo.libraries.service.command.test

import org.axonframework.test.aggregate.AggregateTestFixture

open class AggregateTest<A>(aggregateType: Class<A>) {
    val aggregate: AggregateTestFixture<A> = AggregateTestFixture(aggregateType)
}
