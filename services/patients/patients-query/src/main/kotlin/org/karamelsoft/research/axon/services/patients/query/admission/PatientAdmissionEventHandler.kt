package org.karamelsoft.research.axon.services.patients.query.admission

import org.karamelsoft.research.axon.services.patients.api.admission.AdmissionClosed
import org.karamelsoft.research.axon.services.patients.api.admission.AdmissionCommented
import org.karamelsoft.research.axon.services.patients.api.admission.NewAdmissionRegistered
import org.karamelsoft.research.axon.services.patients.query.PatientRepository
import org.karamelsoft.research.axon.services.patients.query.findBy
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
internal class PatientAdmissionEventHandler(private val repository: PatientRepository) {

    @EventHandler
    fun handle(event: NewAdmissionRegistered) {
        repository.findBy(event.patientId)?.handle(event)
    }

    @EventHandler
    fun handle(event: AdmissionCommented) {
        repository.findBy(event.patientId)?.handle(event)
    }

    @EventHandler
    fun handle(event: AdmissionClosed) {
        repository.findBy(event.patientId)?.handle(event)
    }
}