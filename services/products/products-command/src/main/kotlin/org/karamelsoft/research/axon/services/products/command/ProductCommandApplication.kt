package org.karamelsoft.research.axon.services.products.command

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class ProductCommandApplication

fun main(args: Array<String>) {
    runApplication<ProductCommandApplication>(*args)
}
