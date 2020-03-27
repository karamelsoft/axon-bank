package org.karamelsoft.research.axon.services.patients.command

import org.karamelsoft.research.axon.services.patients.api.CorrectPatientDetails
import org.karamelsoft.research.axon.services.patients.api.NewPatientRegistered
import org.karamelsoft.research.axon.services.patients.api.PatientId
import org.karamelsoft.research.axon.services.patients.api.RegisterNewPatient
import org.karamelsoft.research.axon.services.patients.api.admission.AdmissionId
import org.karamelsoft.research.axon.services.patients.api.admission.NewAdmissionRegistered
import org.karamelsoft.research.axon.services.patients.api.admission.RegisterNewAdmission
import org.karamelsoft.research.axon.services.patients.command.admission.PatientAdmission
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.modelling.command.ForwardMatchingInstances
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
internal class Patient() {

    @AggregateIdentifier
    private lateinit var id: PatientId

    @AggregateMember(
        type = PatientAdmission::class,
        forwardCommands = true,
        eventForwardingMode = ForwardMatchingInstances::class
    )
    private var admissions: MutableMap<AdmissionId, PatientAdmission> = mutableMapOf()

    @CommandHandler
    constructor(command: RegisterNewPatient) : this() {
        command.accepted()
    }

    @CommandHandler
    fun process(command: CorrectPatientDetails) {
        if (command.newDetails.areEmpty()) command.rejectedBy(EmptyPatientDetailsCorrectionException())

        command.accepted()
    }

    @CommandHandler
    fun process(command: RegisterNewAdmission) {
        if (admissions.values.any(PatientAdmission::isOpened)) command.rejectedBy(OpenedAdmissionException())
        if (admissions[command.admissionId] != null) command.rejectedBy(DuplicateAdmissionException())

        command.accepted()
    }

    @EventSourcingHandler
    fun handle(event: NewPatientRegistered) {
        id = event.patientId
    }

    @EventSourcingHandler
    fun handle(event: NewAdmissionRegistered) {
        admissions[event.admissionId] = PatientAdmission(event.admissionId)
    }
}

class EmptyPatientDetailsCorrectionException(
    override val message: String = "Empty correction is not allowed"
) : Exception(message)

class DuplicateAdmissionException(
    override val message: String = "Admission already exists"
) : Exception(message)

class OpenedAdmissionException(
    override val message: String = "One admission is already opened"
) : Exception(message)