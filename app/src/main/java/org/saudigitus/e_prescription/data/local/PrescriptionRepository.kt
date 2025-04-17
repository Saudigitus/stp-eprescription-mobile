package org.saudigitus.e_prescription.data.local

import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance
import org.saudigitus.e_prescription.data.model.Prescription
import java.io.Serializable

interface PrescriptionRepository {
    suspend fun savePrescription(
        event: String,
        dataElement: String,
        value: String,
    )
    suspend fun getPrescriptions(
        tei: String,
        program: String,
        stage: String,
    ): List<Prescription>

    suspend fun getTei(uid:String, program: String): TrackedEntityInstance?
    suspend fun getPrescriptionPatient(uid: String,program: String): TrackedEntityInstance?
}