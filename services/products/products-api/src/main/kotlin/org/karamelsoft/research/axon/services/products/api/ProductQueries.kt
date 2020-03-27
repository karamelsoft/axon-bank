package org.karamelsoft.research.axon.services.products.api

data class DoesProductExists(val productId: ProductId)
data class ProductExistenceResponse(val exists: Boolean)

data class GetProductDetails(val productId: ProductId)
data class ProductDetailsResponse(val details: ProductDetails?)

object ListAllProductIds
data class ProductListResponse(val ids: List<ProductId> = listOf())