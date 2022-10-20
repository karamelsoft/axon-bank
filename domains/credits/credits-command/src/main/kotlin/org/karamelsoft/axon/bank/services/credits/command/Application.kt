package org.karamelsoft.axon.bank.services.credits.command

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class CreditsCommandApplication

fun main(args: Array<String>) {
    runApplication<CreditsCommandApplication>(*args)
}
