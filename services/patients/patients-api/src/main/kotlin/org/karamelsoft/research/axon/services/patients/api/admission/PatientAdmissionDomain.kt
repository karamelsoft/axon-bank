package org.karamelsoft.research.axon.services.patients.api.admission

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")

data class AdmissionId(val reference: String = dateTimeFormatter.format(LocalDateTime.now()))

data class AdmissionDetails(
    val reason: String,
    val startTimestamp: LocalDateTime = LocalDateTime.now(),
    val state: AdmissionState = AdmissionState.OPENED,
    val endTimestamp: LocalDateTime? = null
)

enum class AdmissionState {
    OPENED,
    CLOSED
}

data class AdmissionComment(val comment: String, val timestamp: LocalDateTime)

fun String.toAdmissionId() = AdmissionId(this)
fun LocalDateTime.asString() = dateTimeFormatter.format(this)
fun LocalDateTime.toAdmissionId() = AdmissionId(this.asString())
