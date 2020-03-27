package org.karamelsoft.research.axon.services.patients.query.admission

import org.karamelsoft.research.axon.libraries.service.query.required
import org.karamelsoft.research.axon.services.patients.api.admission.*
import org.karamelsoft.research.axon.services.patients.query.Patient
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "patient_admissions")
data class PatientAdmission(

    @ManyToOne
    var patient: Patient? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,

    var state: AdmissionState? = null,

    var reason: String? = null,

    var startTimestamp: LocalDateTime? = null,

    var endTimestamp: LocalDateTime? = null,

    @OneToMany(
        mappedBy = "admission",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    val comments: MutableList<PatientAdmissionComment> = mutableListOf(),

    val reference: String? = startTimestamp?.asString()
) {
    companion object {
        fun from(event: NewAdmissionRegistered, patient: Patient) = PatientAdmission(
            patient = patient,
            state = event.details.state,
            reason = event.details.reason,
            startTimestamp = event.details.startTimestamp,
            endTimestamp = event.details.endTimestamp
        )
    }

    fun toAdmissionDetails() = AdmissionDetails(
        state = state.required("state"),
        reason = reason.required("reason"),
        startTimestamp = startTimestamp.required("startTimestamp"),
        endTimestamp = endTimestamp
    )

    fun toCommentsList() = comments.map(PatientAdmissionComment::toAdmissionComment)

    fun handle(event: AdmissionCommented) {
        comments.add(PatientAdmissionComment.from(event, this))
    }

    fun handle(event: AdmissionClosed) {
        state = AdmissionState.CLOSED
    }
}

@Entity
@Table(name = "patient_admission_comments")
data class PatientAdmissionComment(

    @ManyToOne
    var admission: PatientAdmission? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,

    var comment: String? = null,

    var timestamp: LocalDateTime? = null
) {
    companion object {
        fun from(event: AdmissionCommented, admission: PatientAdmission) = PatientAdmissionComment(
            admission = admission,
            comment = event.comment,
            timestamp = event.timestamp
        )
    }

    fun toAdmissionComment() = AdmissionComment(
        comment = comment.required("comment"),
        timestamp = timestamp.required("timestamp")
    )
}