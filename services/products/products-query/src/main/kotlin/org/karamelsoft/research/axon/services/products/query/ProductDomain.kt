package org.karamelsoft.research.axon.services.products.query

import org.karamelsoft.research.axon.libraries.service.query.required
import org.karamelsoft.research.axon.services.products.api.*
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Repository
interface ProductRepository: CrudRepository<Product, String>

fun ProductRepository.findBy(productId: ProductId): Product? = this.findById(productId.id).orElse(null)

@Entity
@Table(name = "products")
data class Product(

    @Id
    var id: String? = null,

    @Column(unique = true, nullable = false)
    var reference: String? = null,

    @Column(unique = true, nullable = false)
    var name: String? = null,

    @Column(nullable = true)
    var quantity: Int? = null,

    @Column(nullable = true)
    var unit: ProductUnit? = null
) {
    companion object {
        fun from(event: NewProductRegistered) = Product(
            id = event.productId.id,
            reference = event.details.reference,
            name = event.details.name,
            quantity = event.details.quantity,
            unit = event.details.unit
        )
    }

    fun toProductDetails() = ProductDetails(
        reference = reference.required("reference"),
        name = name.required("name"),
        quantity = quantity,
        unit = unit
    )

    fun handle(event: ProductDetailsCorrected) {
        event.newDetails.newReference?.let { newReference -> reference = newReference }
        event.newDetails.newName?.let { newName -> name = newName }
        event.newDetails.newQuantity?.let { newQuantity -> quantity = newQuantity }
        event.newDetails.newUnit?.let { newUnit -> unit = newUnit }
    }
}