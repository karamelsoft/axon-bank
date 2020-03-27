package org.karamelsoft.research.axon.services.patients.api.admission

import org.karamelsoft.research.axon.services.patients.api.PatientId

data class GetAdmissionDetails(val patientId: PatientId, val admissionId: AdmissionId)
data class AdmissionDetailsResponse(val details: AdmissionDetails?)

data class GetAdmissionComments(val patientId: PatientId, val admissionId: AdmissionId)
data class AdmissionCommentsResponse(val comments: List<AdmissionComment>?)

data class ListAllPatientAdmissions(val patientId: PatientId)
data class AdmissionListResponse(val ids: List<AdmissionId>?)