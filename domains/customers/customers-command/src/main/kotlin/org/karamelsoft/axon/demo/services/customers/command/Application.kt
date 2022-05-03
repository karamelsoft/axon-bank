package org.karamelsoft.axon.demo.services.customers.command

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class CustomersCommandApplication

fun main(args: Array<String>) {
    runApplication<CustomersCommandApplication>(*args)
}
