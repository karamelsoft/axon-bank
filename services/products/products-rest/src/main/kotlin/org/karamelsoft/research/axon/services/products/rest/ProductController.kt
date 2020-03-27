package org.karamelsoft.research.axon.services.products.rest

import org.karamelsoft.research.axon.libraries.service.rest.toCommandResponse
import org.karamelsoft.research.axon.libraries.service.rest.toQueryResponse
import org.karamelsoft.research.axon.services.products.api.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("products")
internal class ProductController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
    private val eventStore: EventStore
) {

    @GetMapping("")
    fun getAllProducts(): CompletableFuture<ResponseEntity<List<ProductId>>> {
        return queryGateway.query(
            ListAllProductIds,
            ProductListResponse::class.java
        ).thenApply(ProductListResponse::ids).toQueryResponse()
    }

    @PostMapping("")
    fun registerNewProduct(@RequestBody body: ProductDetails): CompletableFuture<ResponseEntity<ProductId>> {
        return commandGateway.send<ProductId>(
            RegisterNewProduct(
                productId = ProductId(),
                details = body
            )
        ).toCommandResponse()
    }

    @PutMapping("{product_id}")
    fun registerNewProductWithId(
        @PathVariable("product_id") id: String,
        @RequestBody body: ProductDetails
    ): CompletableFuture<ResponseEntity<ProductId>> {
        return commandGateway.send<ProductId>(
            RegisterNewProduct(
                productId = id.toProductId(),
                details = body
            )
        ).toCommandResponse()
    }

    @GetMapping("{product_id}/details")
    fun getProduct(@PathVariable("product_id") id: String): CompletableFuture<ResponseEntity<ProductDetails?>> {
        return queryGateway.query(
            GetProductDetails(id.toProductId()),
            ProductDetailsResponse::class.java
        ).thenApply(ProductDetailsResponse::details).toQueryResponse()
    }

    @GetMapping("{product_id}/events")
    fun getProductEvents(@PathVariable("product_id") id: String): List<*> {
        return eventStore.readEvents(id.toProductId().toString()).asSequence().toList()
    }

    @PatchMapping("{product_id}/details")
    fun correctProductDetails(
        @PathVariable("product_id") id: String,
        @RequestBody body: ProductDetailsCorrection
    ): CompletableFuture<ResponseEntity<Unit>> {
        return commandGateway.send<Unit>(
            CorrectProductDetails(
                productId = id.toProductId(),
                newDetails = body
            )
        ).toCommandResponse()
    }
}
