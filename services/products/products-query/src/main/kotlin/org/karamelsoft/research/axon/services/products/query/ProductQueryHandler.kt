package org.karamelsoft.research.axon.services.products.query

import org.karamelsoft.research.axon.services.products.api.*
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
internal class ProductQueryHandler(val repository: ProductRepository) {

    @QueryHandler
    fun handle(query: DoesProductExists) = ProductExistenceResponse(
        exists = repository.findBy(query.productId)
            ?.let { true }
            ?: false
    )

    @QueryHandler
    fun handle(query: GetProductDetails) = ProductDetailsResponse(
        details = repository.findBy(query.productId)
            ?.toProductDetails()
    )

    @QueryHandler
    fun handle(query: ListAllProductIds) = ProductListResponse(
        ids = repository.findAll().asSequence()
            .map(Product::id)
            .filterNotNull()
            .map(String::toProductId)
            .toList()
    )
}