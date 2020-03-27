package org.karamelsoft.research.axon.services.patients.api

import java.time.LocalDate
import java.util.*

data class PatientId(val id: String = UUID.randomUUID().toString())

data class PatientDetails(
    val firstName: String,
    val lastName: String,
    val sex: Sex,
    val birthDate: LocalDate
)

data class PatientDetailsCorrection(
    val newFirstName: String? = null,
    val newLastName: String? = null,
    val newSex: Sex? = null,
    val newBirthDate: LocalDate? = null
) {
    fun areEmpty() = newFirstName == null
            && newLastName == null
            && newSex == null
            && newBirthDate == null
}

enum class Sex {
    MALE,
    FEMALE
}

fun String.toPatientId() = PatientId(this)