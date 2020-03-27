package org.karamelsoft.research.axon.services.patients.query

import org.karamelsoft.research.axon.services.patients.api.*
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
internal class PatientQueryHandler(val repository: PatientRepository) {

    @QueryHandler
    fun handle(query: GetPatientDetails) = PatientDetailsResponse(
        details = repository.findBy(query.patientId)
            ?.toPatientDetails()
    )

    @QueryHandler
    fun handle(query: ListAllPatientIds) = PatientListResponse(
        ids = repository.findAll().asSequence()
            .map(Patient::id)
            .filterNotNull()
            .map(String::toPatientId)
            .toList()
    )
}