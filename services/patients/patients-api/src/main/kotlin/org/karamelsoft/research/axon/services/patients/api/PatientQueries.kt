package org.karamelsoft.research.axon.services.patients.api

data class GetPatientDetails(val patientId: PatientId)
data class PatientDetailsResponse(val details: PatientDetails?)

object ListAllPatientIds
data class PatientListResponse(val ids: List<PatientId> = listOf())