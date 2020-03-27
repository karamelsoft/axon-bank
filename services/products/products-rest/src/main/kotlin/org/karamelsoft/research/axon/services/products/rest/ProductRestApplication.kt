package org.karamelsoft.research.axon.services.products.rest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class ProductRestApplication

fun main(args: Array<String>) {
    runApplication<ProductRestApplication>(*args)
}
