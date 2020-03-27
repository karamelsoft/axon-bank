package org.karamelsoft.research.axon.services.products.query

import org.karamelsoft.research.axon.services.products.api.NewProductRegistered
import org.karamelsoft.research.axon.services.products.api.ProductDetailsCorrected
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
internal class ProductEventHandler(private val repository: ProductRepository) {

    @EventHandler
    fun handle(event: NewProductRegistered) {
        repository.save(Product.from(event))
    }

    @EventHandler
    fun handle(event: ProductDetailsCorrected) {
        repository.findBy(event.productId)?.handle(event)
    }
}