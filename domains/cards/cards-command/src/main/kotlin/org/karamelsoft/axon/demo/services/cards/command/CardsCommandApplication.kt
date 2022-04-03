package org.karamelsoft.axon.demo.services.cards.command

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class CardsCommandApplication

fun main(args: Array<String>) {
    runApplication<CardsCommandApplication>(*args)
}
