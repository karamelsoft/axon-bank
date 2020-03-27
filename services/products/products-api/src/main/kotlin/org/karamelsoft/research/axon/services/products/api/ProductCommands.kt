package org.karamelsoft.research.axon.services.products.api

import org.karamelsoft.research.axon.libraries.service.api.Command
import org.karamelsoft.research.axon.libraries.service.api.Event
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalDateTime

interface ProductCommand: Command {
    val productId: ProductId
}

interface ProductEvent: Event {
    val productId: ProductId
}

data class RegisterNewProduct(
    @TargetAggregateIdentifier
    override val productId: ProductId,
    val details: ProductDetails,
    override val timestamp: LocalDateTime = LocalDateTime.now()
): ProductCommand {
    fun toNewProductRegistered(timestamp: LocalDateTime = LocalDateTime.now()) = NewProductRegistered(
        productId = productId,
        details = details,
        timestamp = timestamp
    )

    override fun toEvents(timestamp: LocalDateTime) = listOf(
        toNewProductRegistered(timestamp)
    )
}

data class NewProductRegistered(
    @TargetAggregateIdentifier
    override val productId: ProductId,
    val details: ProductDetails,
    override val timestamp: LocalDateTime = LocalDateTime.now()
): ProductEvent

data class CorrectProductDetails(
    @TargetAggregateIdentifier
    override val productId: ProductId,
    val newDetails: ProductDetailsCorrection,
    override val timestamp: LocalDateTime = LocalDateTime.now()
): ProductCommand {
    fun toProductNameCorrected(timestamp: LocalDateTime = LocalDateTime.now()) = ProductDetailsCorrected(
        productId = productId,
        newDetails = newDetails,
        timestamp = timestamp
    )

    override fun toEvents(timestamp: LocalDateTime) = listOf(
        toProductNameCorrected(timestamp)
    )
}

data class ProductDetailsCorrected(
    @TargetAggregateIdentifier
    override val productId: ProductId,
    val newDetails: ProductDetailsCorrection,
    override val timestamp: LocalDateTime = LocalDateTime.now()
): ProductEvent
