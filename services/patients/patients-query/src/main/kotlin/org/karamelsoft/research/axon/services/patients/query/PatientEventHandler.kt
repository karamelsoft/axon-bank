package org.karamelsoft.research.axon.services.patients.query

import org.karamelsoft.research.axon.services.patients.api.NewPatientRegistered
import org.karamelsoft.research.axon.services.patients.api.PatientDetailsCorrected
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
internal class PatientEventHandler(private val repository: PatientRepository) {

    @EventHandler
    fun handle(event: NewPatientRegistered) {
        repository.save(Patient.from(event))
    }

    @EventHandler
    fun handle(event: PatientDetailsCorrected) {
        repository.findBy(event.patientId)?.handle(event)
    }
}