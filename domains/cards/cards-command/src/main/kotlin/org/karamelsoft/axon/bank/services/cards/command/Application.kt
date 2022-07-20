package org.karamelsoft.axon.bank.services.cards.command

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
internal class CardsCommandApplication {

    @Bean
    fun cardSnapshotter(snapshotter: Snapshotter): EventCountSnapshotTriggerDefinition = EventCountSnapshotTriggerDefinition(snapshotter, 10)
}

fun main(args: Array<String>) {
    runApplication<CardsCommandApplication>(*args)
}
