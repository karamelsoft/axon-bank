package org.karamelsoft.research.axon.services.products.api

import java.util.*

data class ProductId(val id: String = UUID.randomUUID().toString())

data class ProductDetails(
    val reference: String,
    val name: String,
    val quantity: Int? = null,
    val unit: ProductUnit? = null
)

data class ProductDetailsCorrection(
    val newReference: String? = null,
    val newName: String? = null,
    val newQuantity: Int? = null,
    val newUnit: ProductUnit? = null
) {
    fun areEmpty() = newReference == null
            && newName == null
            && newQuantity == null
            && newUnit == null
}

fun String.toProductId() = ProductId(this)

enum class ProductUnit(s: String) {
    MG("mg"),
    G("g"),
    ML("ml"),
    L("l"),
}