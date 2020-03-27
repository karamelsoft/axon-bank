package org.karamelsoft.research.axon.services.patients.query

import org.karamelsoft.research.axon.libraries.service.query.required
import org.karamelsoft.research.axon.services.patients.api.*
import org.karamelsoft.research.axon.services.patients.api.admission.AdmissionClosed
import org.karamelsoft.research.axon.services.patients.api.admission.AdmissionCommented
import org.karamelsoft.research.axon.services.patients.api.admission.AdmissionId
import org.karamelsoft.research.axon.services.patients.api.admission.NewAdmissionRegistered
import org.karamelsoft.research.axon.services.patients.query.admission.PatientAdmission
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.*

@Repository
interface PatientRepository: CrudRepository<Patient, String>

fun PatientRepository.findBy(patientId: PatientId): Patient? = this.findById(patientId.id).orElse(null)

@Entity
@Table(name = "patients")
data class Patient(

    @Id
    var id: String? = null,

    @Column(nullable = false)
    var firstName: String? = null,

    @Column(nullable = false)
    var lastName: String? = null,

    @Column(nullable = false)
    var birthDate: LocalDate? = null,

    @Column(nullable = false)
    var sex: Sex? = null,

    @OneToMany(
        mappedBy = "patient",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @MapKey(name = "reference")
    var admissions: MutableMap<String, PatientAdmission> = mutableMapOf()
) {
    companion object {
        fun from(event: NewPatientRegistered) = Patient(
            id = event.patientId.id,
            firstName = event.details.firstName,
            lastName = event.details.lastName,
            birthDate = event.details.birthDate,
            sex = event.details.sex
        )
    }

    fun toPatientDetails() = PatientDetails(
        firstName = firstName.required("firstName"),
        lastName = lastName.required("lastName"),
        birthDate = birthDate.required("birthDate"),
        sex = sex .required("sex")
    )

    fun findAdmission(admissionId: AdmissionId) = admissions[admissionId.reference]

    fun handle(event: PatientDetailsCorrected) {
        event.newDetails.newFirstName?.let { newFirstName -> firstName = newFirstName }
        event.newDetails.newLastName?.let { newLastName -> lastName = newLastName }
        event.newDetails.newBirthDate?.let { newBirthDate -> birthDate = newBirthDate }
        event.newDetails.newSex?.let { newSex -> sex = newSex }
    }

    fun handle(event: NewAdmissionRegistered) {
        admissions[event.admissionId.reference] = PatientAdmission.from(event, this)
    }

    fun handle(event: AdmissionCommented) {
        admissions[event.admissionId.reference]?.handle(event)
    }

    fun handle(event: AdmissionClosed) {
        admissions[event.admissionId.reference]?.handle(event)
    }
}