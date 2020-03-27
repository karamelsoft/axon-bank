package org.karamelsoft.research.axon.services.patients.command.admission

import org.karamelsoft.research.axon.services.patients.api.admission.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.EntityId

internal class PatientAdmission(
    @EntityId
    private val admissionId: AdmissionId,
    private var state: AdmissionState = AdmissionState.OPENED
) {

    @CommandHandler
    fun process(command: CommentAdmission) = when (state) {
        AdmissionState.OPENED -> command.accepted()
        AdmissionState.CLOSED -> command.rejectedBy(AdmissionClosedException())
    }

    @CommandHandler
    fun process(command: CloseAdmission) = when (state) {
        AdmissionState.OPENED -> command.accepted()
        AdmissionState.CLOSED -> command.rejectedBy(AdmissionClosedException())
    }

    @EventSourcingHandler
    fun handle(event: AdmissionClosed) {
        state = AdmissionState.CLOSED
    }

    fun isOpened() = state == AdmissionState.OPENED
}