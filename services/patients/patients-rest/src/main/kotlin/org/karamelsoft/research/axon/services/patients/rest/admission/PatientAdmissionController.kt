package org.karamelsoft.research.axon.services.patients.rest.admission

import org.karamelsoft.research.axon.libraries.service.rest.toCommandResponse
import org.karamelsoft.research.axon.libraries.service.rest.toQueryResponse
import org.karamelsoft.research.axon.services.patients.api.admission.*
import org.karamelsoft.research.axon.services.patients.api.toPatientId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("patients/{patient_id}/admissions")
internal class PatientAdmissionController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {

    @GetMapping("")
    fun getAllAdmissions(@PathVariable("patient_id") patientId: String): CompletableFuture<ResponseEntity<List<AdmissionId>?>> {
        return queryGateway.query(
            ListAllPatientAdmissions(patientId = patientId.toPatientId()),
            AdmissionListResponse::class.java
        ).thenApply(AdmissionListResponse::ids).toQueryResponse()
    }

    @PostMapping("")
    fun registerNewAdmission(
        @PathVariable("patient_id") patientId: String,
        @RequestBody body: AdmissionDetails
    ): CompletableFuture<ResponseEntity<AdmissionId>> {
        val admissionId = body.startTimestamp.toAdmissionId()
        return commandGateway.send<Unit>(
            RegisterNewAdmission(
                patientId = patientId.toPatientId(),
                admissionId = admissionId,
                details = body
            )
        ).thenApply { admissionId }.toCommandResponse()
    }

    @PutMapping("{admission_id}")
    fun registerNewAdmissionWithId(
        @PathVariable("patient_id") patientId: String,
        @PathVariable("admission_id") admissionId: String,
        @RequestBody body: AdmissionDetails
    ): CompletableFuture<ResponseEntity<Unit>> {
        return commandGateway.send<Unit>(
            RegisterNewAdmission(
                patientId = patientId.toPatientId(),
                admissionId = admissionId.toAdmissionId(),
                details = body
            )
        ).toCommandResponse()
    }

    @DeleteMapping("{admission_id}")
    fun closeAdmission(
        @PathVariable("patient_id") patientId: String,
        @PathVariable("admission_id") admissionId: String,
        @RequestBody comment: String?
    ): CompletableFuture<ResponseEntity<Unit>> {
        return commandGateway.send<Unit>(
            CloseAdmission(
                patientId = patientId.toPatientId(),
                admissionId = admissionId.toAdmissionId(),
                comment = comment
            )
        ).toCommandResponse()
    }

    @GetMapping("{admission_id}/details")
    fun getAdmission(
        @PathVariable("patient_id") patientId: String,
        @PathVariable("admission_id") admissionId: String
    ): CompletableFuture<ResponseEntity<AdmissionDetails?>> {
        return queryGateway.query(
            GetAdmissionDetails(
                patientId = patientId.toPatientId(),
                admissionId = admissionId.toAdmissionId()
            ),
            AdmissionDetailsResponse::class.java
        ).thenApply(AdmissionDetailsResponse::details).toQueryResponse()
    }

    @PostMapping("{admission_id}/comments")
    fun commentAdmission(
        @PathVariable("patient_id") patientId: String,
        @PathVariable("admission_id") admissionId: String,
        @RequestBody comment: String
    ): CompletableFuture<ResponseEntity<Unit>> {
        return commandGateway.send<Unit>(
            CommentAdmission(
                patientId = patientId.toPatientId(),
                admissionId = admissionId.toAdmissionId(),
                comment = comment
            )
        ).toCommandResponse()
    }

    @GetMapping("{admission_id}/comments")
    fun getAdmissionComments(
        @PathVariable("patient_id") patientId: String,
        @PathVariable("admission_id") admissionId: String
    ): CompletableFuture<ResponseEntity<List<AdmissionComment>?>> {
        return queryGateway.query(
            GetAdmissionComments(
                patientId = patientId.toPatientId(),
                admissionId = admissionId.toAdmissionId()
            ),
            AdmissionCommentsResponse::class.java
        ).thenApply(AdmissionCommentsResponse::comments).toQueryResponse()
    }
}
