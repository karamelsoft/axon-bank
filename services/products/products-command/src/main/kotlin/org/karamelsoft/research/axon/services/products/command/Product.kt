package org.karamelsoft.research.axon.services.products.command

import org.karamelsoft.research.axon.services.products.api.CorrectProductDetails
import org.karamelsoft.research.axon.services.products.api.NewProductRegistered
import org.karamelsoft.research.axon.services.products.api.ProductId
import org.karamelsoft.research.axon.services.products.api.RegisterNewProduct
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
internal class Product() {

    @AggregateIdentifier
    private lateinit var id: ProductId

    @CommandHandler
    constructor(command: RegisterNewProduct) : this() {
        command.accepted()
    }

    @CommandHandler
    fun process(command: CorrectProductDetails) {
        if (command.newDetails.areEmpty()) {
            command.rejectedBy(EmptyProductDetailsCorrectionException())
        }

        command.accepted()
    }

    @EventSourcingHandler
    fun handle(event: NewProductRegistered) {
        id = event.productId
    }
}

class EmptyProductDetailsCorrectionException(override val message: String = "Empty correction is not allowed") :
    Exception(message)