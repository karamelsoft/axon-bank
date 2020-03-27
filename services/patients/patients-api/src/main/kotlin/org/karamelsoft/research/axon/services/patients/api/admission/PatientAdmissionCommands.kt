package org.karamelsoft.research.axon.services.patients.api.admission

import org.karamelsoft.research.axon.services.patients.api.PatientCommand
import org.karamelsoft.research.axon.services.patients.api.PatientEvent
import org.karamelsoft.research.axon.services.patients.api.PatientId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalDateTime

interface PatientAdmissionCommand : PatientCommand {
    val admissionId: AdmissionId
}

interface PatientAdmissionEvent : PatientEvent {
    val admissionId: AdmissionId
}

class AdmissionClosedException(override val message: String = "Admission closed") : Exception(message)

data class RegisterNewAdmission(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    override val admissionId: AdmissionId,
    val details: AdmissionDetails,
    override val timestamp: LocalDateTime = LocalDateTime.now()
) : PatientAdmissionCommand {
    fun toNewAdmissionRegistered(timestamp: LocalDateTime = LocalDateTime.now()) = NewAdmissionRegistered(
        patientId = patientId,
        admissionId = admissionId,
        details = details,
        timestamp = timestamp
    )

    override fun toEvents(timestamp: LocalDateTime) = listOf(
        toNewAdmissionRegistered(timestamp)
    )
}

data class NewAdmissionRegistered(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    override val admissionId: AdmissionId,
    val details: AdmissionDetails,
    override val timestamp: LocalDateTime = LocalDateTime.now()
) : PatientAdmissionEvent

data class CommentAdmission(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    override val admissionId: AdmissionId,
    val comment: String,
    override val timestamp: LocalDateTime = LocalDateTime.now()
) : PatientAdmissionCommand {
    fun toAdmissionCommented(timestamp: LocalDateTime = LocalDateTime.now()) = AdmissionCommented(
        patientId = patientId,
        admissionId = admissionId,
        comment = comment,
        timestamp = timestamp
    )

    override fun toEvents(timestamp: LocalDateTime) = listOf(
        toAdmissionCommented(timestamp)
    )
}

data class AdmissionCommented(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    override val admissionId: AdmissionId,
    val comment: String,
    override val timestamp: LocalDateTime = LocalDateTime.now()
) : PatientAdmissionEvent

data class CloseAdmission(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    override val admissionId: AdmissionId,
    val comment: String?,
    override val timestamp: LocalDateTime = LocalDateTime.now()
) : PatientAdmissionCommand {
    fun toAdmissionCommented(timestamp: LocalDateTime = LocalDateTime.now()) = comment?.let {
        AdmissionCommented(
            patientId = patientId,
            admissionId = admissionId,
            comment = comment,
            timestamp = timestamp
        )
    }

    fun toAdmissionClosed(timestamp: LocalDateTime = LocalDateTime.now()) = AdmissionClosed(
        patientId = patientId,
        admissionId = admissionId,
        timestamp = timestamp
    )

    override fun toEvents(timestamp: LocalDateTime) = listOfNotNull(
        toAdmissionCommented(timestamp),
        toAdmissionClosed(timestamp)
    )
}

data class AdmissionClosed(
    @TargetAggregateIdentifier
    override val patientId: PatientId,
    override val admissionId: AdmissionId,
    override val timestamp: LocalDateTime = LocalDateTime.now()
) : PatientAdmissionEvent