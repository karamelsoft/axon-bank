package org.karamelsoft.axon.demo.services.accounts.command

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
internal class AccountsCommandApplication {

    @Bean
    fun accountSnapshotter(snapshotter: Snapshotter): EventCountSnapshotTriggerDefinition = EventCountSnapshotTriggerDefinition(snapshotter, 10)
}

fun main(args: Array<String>) {
    runApplication<AccountsCommandApplication>(*args)
}
