package org.saudigitus.e_prescription.data.local.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.event.EventStatus
import org.hisp.dhis.android.core.relationship.Relationship
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance
import org.saudigitus.e_prescription.data.local.PrescriptionRepository
import org.saudigitus.e_prescription.data.model.Patient
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.utils.AttributesHalper
import org.saudigitus.e_prescription.utils.NetworkUtils
import org.saudigitus.e_prescription.utils.UIDMapping
import org.saudigitus.e_prescription.utils.eventsWithTrackedDataValues
import javax.inject.Inject


class PrescriptionRepositoryImpl(
    private val d2: D2,
    private val networkUtils: NetworkUtils,
    private val attributesHapler: AttributesHalper
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

        d2.trackedEntityModule().trackedEntityInstanceDownloader()
            .byUid().`in`(tei)
            .byProgramUid(program)
            .blockingDownload()

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

    override suspend fun getPrescriptionPatient(
        uid: String,
        program: String
    ):Patient? = withContext(Dispatchers.IO) {
        val repository = d2.trackedEntityModule().trackedEntityInstanceQuery()
        val relationships = d2.relationshipModule().relationships()
            .byRelationshipType().eq(UIDMapping.RELATIONSHIP_TYPE_UID)
            .withItems()
            .blockingGet()

        val trackedEntityInstancesUIds = relationships.filter {
            it.to()?.trackedEntityInstance()?.trackedEntityInstance() == uid
        }.mapNotNull {
            it.from()?.trackedEntityInstance()?.trackedEntityInstance()
        }
        val result = d2.trackedEntityModule()
            .trackedEntityInstances()
            .withTrackedEntityAttributeValues()
            .byUid().eq(trackedEntityInstancesUIds.first())
            .one()
            .blockingGet()

        val teiUid = trackedEntityInstancesUIds.first()

        val patient = Patient(
            uid = teiUid,
            name = attributesHapler.getAttributeValueByCode(tei = result,"Jrd6W0L8LQY").toString(),          // Replace with actual attribute code for name
            surname = attributesHapler.getAttributeValueByCode(tei = result,"KmR2FYgDUmr").toString(),       // Replace with actual code for surname
            residence = attributesHapler.getAttributeValueByCode(tei = result,"HKjREW796JR").toString(),     // Code for residence
            gender = attributesHapler.getAttributeValueByCode(tei = result,"CklPZdOd6H1").toString(),        // Code for gender
            processNumber = attributesHapler.getAttributeValueByCode(tei = result,"um3rU8yasxl") .toString(),// Code for process number
            birthdate = attributesHapler.getAttributeValueByCode(tei = result,"S5YtVz5P3QE") .toString(),    // Code for birthdate
        )
        return@withContext patient
    }
}