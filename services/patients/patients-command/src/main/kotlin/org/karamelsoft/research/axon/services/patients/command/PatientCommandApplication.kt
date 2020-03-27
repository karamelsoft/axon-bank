package org.karamelsoft.research.axon.services.patients.command

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class PatientCommandApplication

fun main(args: Array<String>) {
    runApplication<PatientCommandApplication>(*args)
}
