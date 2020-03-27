package org.karamelsoft.research.axon.services.patients.rest

import org.karamelsoft.research.axon.libraries.service.rest.toCommandResponse
import org.karamelsoft.research.axon.libraries.service.rest.toQueryResponse
import org.karamelsoft.research.axon.services.patients.api.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("patients")
internal class PatientController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
    private val eventStore: EventStore
) {

    @GetMapping("")
    fun getAllPatients(): CompletableFuture<ResponseEntity<List<PatientId>>> {
        return queryGateway.query(
            ListAllPatientIds,
            PatientListResponse::class.java
        ).thenApply(PatientListResponse::ids).toQueryResponse()
    }

    @PostMapping("")
    fun registerNewPatient(@RequestBody body: PatientDetails): CompletableFuture<ResponseEntity<PatientId>> {
        return commandGateway.send<PatientId>(
            RegisterNewPatient(
                patientId = PatientId(),
                details = body
            )
        ).toCommandResponse()
    }

    @PutMapping("{patient_id}")
    fun registerNewPatientWithId(
        @PathVariable("patient_id") id: String,
        @RequestBody body: PatientDetails
    ): CompletableFuture<ResponseEntity<PatientId>> {
        return commandGateway.send<PatientId>(
            RegisterNewPatient(
                patientId = id.toPatientId(),
                details = body
            )
        ).toCommandResponse()
    }

    @GetMapping("{patient_id}/details")
    fun getPatient(@PathVariable("patient_id") id: String): CompletableFuture<ResponseEntity<PatientDetails?>> {
        return queryGateway.query(
            GetPatientDetails(id.toPatientId()),
            PatientDetailsResponse::class.java
        ).thenApply(PatientDetailsResponse::details).toQueryResponse()
    }

    @GetMapping("{patient_id}/events")
    fun getPatientEvents(@PathVariable("patient_id") id: String): List<*> {
        return eventStore.readEvents(id.toPatientId().toString()).asSequence().toList()
    }

    @PatchMapping("{patient_id}/details")
    fun correctPatientDetails(
        @PathVariable("patient_id") id: String,
        @RequestBody body: PatientDetailsCorrection
    ): CompletableFuture<ResponseEntity<Unit>> {
        return commandGateway.send<Unit>(
            CorrectPatientDetails(
                patientId = id.toPatientId(),
                newDetails = body
            )
        ).toCommandResponse()
    }
}
