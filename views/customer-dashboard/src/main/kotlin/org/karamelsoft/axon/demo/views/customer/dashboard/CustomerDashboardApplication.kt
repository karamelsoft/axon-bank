package org.karamelsoft.axon.demo.views.customer.dashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class CustomerDashboardApplication

fun main(args: Array<String>) {
    runApplication<CustomerDashboardApplication>(*args)
}
