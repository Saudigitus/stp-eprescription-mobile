package org.saudigitus.e_prescription.data.local.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.event.EventStatus
import org.saudigitus.e_prescription.data.local.PrescriptionRepository
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.utils.UIDMapping
import org.saudigitus.e_prescription.utils.eventsWithTrackedDataValues

class PrescriptionRepositoryImpl(
    private val d2: D2
): PrescriptionRepository {
    override suspend fun savePrescription(
        event: String,
        dataElement: String,
        value: String
    ) = withContext(Dispatchers.IO) {
        d2.trackedEntityModule().trackedEntityDataValues()
            .value(event, dataElement)
            .blockingSet(value)

        val repository = d2.eventModule().events().uid(event)

        d2.eventModule().events().blockingUpload()
    }

    override suspend fun getPrescriptions(
        tei: String,
        program: String,
        stage: String,
    ) = withContext(Dispatchers.IO) {
        d2.eventsWithTrackedDataValues(tei, program, stage)
            .map { event ->
                val name = event.trackedEntityDataValues()?.first { it.dataElement() == UIDMapping.DATA_ELEMENT_NAME }?.value()
                val posology = event.trackedEntityDataValues()?.first { it.dataElement() == UIDMapping.DATA_ELEMENT_POSOLOGY }?.value() ?: ""
                val requestedQtd = event.trackedEntityDataValues()?.first { it.dataElement() == UIDMapping.DATA_ELEMENT_QTD_REQ }?.value() ?: "0"

                val option = d2.optionModule().options()
                    .byCode().eq(name)
                    .one().blockingGet()

                Prescription(
                    uid = event.uid(),
                    name = option?.displayName() ?: "",
                    posology = posology,
                    requestedQtd = requestedQtd.toInt(),
                    isCompleted = event.status() == EventStatus.COMPLETED
                )
            }
    }
}