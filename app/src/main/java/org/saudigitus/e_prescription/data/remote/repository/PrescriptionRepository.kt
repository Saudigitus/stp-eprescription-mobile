package org.saudigitus.e_prescription.data.remote.repository

import org.saudigitus.e_prescription.data.model.Patient
import org.saudigitus.e_prescription.data.model.Prescription

interface PrescriptionRepository {
    suspend fun savePrescription(
        tei: String,
        ou: String,
        event: String,
        dataElement: String,
        value: String,
    ): Boolean
    suspend fun getPrescriptions(
        tei: String,
        program: String,
        stage: String,
    ): List<Prescription>
    suspend fun getPatient(uid: String, program: String, relationshipType: String): Patient?
}