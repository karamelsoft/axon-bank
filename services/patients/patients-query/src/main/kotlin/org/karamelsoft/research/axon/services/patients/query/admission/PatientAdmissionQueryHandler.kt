package org.karamelsoft.research.axon.services.patients.query.admission

import org.karamelsoft.research.axon.services.patients.api.admission.*
import org.karamelsoft.research.axon.services.patients.query.PatientRepository
import org.karamelsoft.research.axon.services.patients.query.findBy
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
internal class PatientAdmissionQueryHandler(val repository: PatientRepository) {

    @QueryHandler
    fun handle(query: GetAdmissionDetails) = AdmissionDetailsResponse(
        details = repository.findBy(query.patientId)
            ?.findAdmission(query.admissionId)
            ?.toAdmissionDetails()
    )

    @QueryHandler
    fun handle(query: GetAdmissionComments) = AdmissionCommentsResponse(
        comments = repository.findBy(query.patientId)
            ?.findAdmission(query.admissionId)
            ?.toCommentsList()
    )

    @QueryHandler
    fun handle(query: ListAllPatientAdmissions) = AdmissionListResponse(
        ids = repository.findBy(query.patientId)
            ?.admissions?.values
            ?.mapNotNull(PatientAdmission::startTimestamp)
            ?.map(LocalDateTime::toAdmissionId)
    )
}