package org.karamelsoft.axon.bank.orchestrators.payment.processor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class PaymentProcessorApplication

fun main(args: Array<String>) {
    runApplication<PaymentProcessorApplication>(*args)
}
