package org.karamelsoft.research.axon.services.patients.api

import org.karamelsoft.research.axon.libraries.service.api.Command
import org.karamelsoft.research.axon.libraries.service.api.Event
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalDateTime

interface PatientCommand: Command {
    val patientId: PatientId
}

interface PatientEvent: Event {
    val patientId: PatientId
}

data class RegisterNewPatient(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    val details: PatientDetails,
    override val timestamp: LocalDateTime = LocalDateTime.now()
): PatientCommand {
    fun toNewPatientRegistered(timestamp: LocalDateTime = LocalDateTime.now()) = NewPatientRegistered(
        patientId = patientId,
        details = details,
        timestamp = timestamp
    )

    override fun toEvents(timestamp: LocalDateTime) = listOf(
        toNewPatientRegistered(timestamp)
    )

}

data class NewPatientRegistered(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    val details: PatientDetails,
    override val timestamp: LocalDateTime = LocalDateTime.now()
): PatientEvent

data class CorrectPatientDetails(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    val newDetails: PatientDetailsCorrection,
    override val timestamp: LocalDateTime = LocalDateTime.now()
): PatientCommand {
    fun toPatientDetailsCorrected(timestamp: LocalDateTime = LocalDateTime.now()) = PatientDetailsCorrected(
        patientId = patientId,
        newDetails = newDetails,
        timestamp = timestamp
    )

    override fun toEvents(timestamp: LocalDateTime) = listOf(
        toPatientDetailsCorrected(timestamp)
    )
}

data class PatientDetailsCorrected(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    val newDetails: PatientDetailsCorrection,
    override val timestamp: LocalDateTime = LocalDateTime.now()
): PatientEvent