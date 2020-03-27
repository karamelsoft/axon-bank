package org.karamelsoft.research.axon.services.patients.rest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class PatientRestApplication

fun main(args: Array<String>) {
    runApplication<PatientRestApplication>(*args)
}
