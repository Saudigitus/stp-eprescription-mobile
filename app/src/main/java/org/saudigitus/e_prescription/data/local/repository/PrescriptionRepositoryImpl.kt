package org.saudigitus.e_prescription.data.local.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.event.EventStatus
import org.hisp.dhis.android.core.relationship.Relationship
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance
import org.saudigitus.e_prescription.data.local.PrescriptionRepository
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.utils.NetworkUtils
import org.saudigitus.e_prescription.utils.UIDMapping
import org.saudigitus.e_prescription.utils.eventsWithTrackedDataValues


class PrescriptionRepositoryImpl(
    private val d2: D2,
    private val networkUtils: NetworkUtils
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

    override suspend fun getTei(
        uid: String,
        program: String
    ): TrackedEntityInstance?  = withContext(Dispatchers.IO) {
        // Log.d("PRESC_VM_GET_TEI_M", "IN GET TEI")

        //getPatients(uid,program)
        val repository = d2.trackedEntityModule().trackedEntityInstanceQuery()

        return@withContext (if (networkUtils.isOnline()) {
            repository.offlineFirst().allowOnlineCache().eq(true)
                .byProgram().eq(program)
                .byTrackedEntities().eq(uid)
                .one()
                .blockingGet()
        } else {
            repository.offlineOnly().allowOnlineCache().eq(false)
                .byProgram().eq(program)
                .byTrackedEntities().eq(uid)
                .one()
                .blockingGet()
        })
    }

    override suspend fun getPrescriptionPatient(
        uid: String,
        program: String
    ):TrackedEntityInstance? = withContext(Dispatchers.IO) {
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

        Log.d("RELATIONSHIPS_TEIS","${trackedEntityInstancesUIds.first()}")

//        val result = d2.trackedEntityModule().trackedEntityInstanceQuery()
//            .allowOnlineCache().eq(false).offlineOnly()
//            .byOrgUnits().eq(UIDMapping.OU_UID)
//            .byProgram().eq(program)
//            .uid(trackedEntityInstancesUIds.first())
//            //.byTrackedEntities().eq(trackedEntityInstancesUIds.first())
//            .one()
//            .blockingGet()

        val result = d2.trackedEntityModule()
            .trackedEntityInstances()
            .withTrackedEntityAttributeValues()
            .byUid().eq(trackedEntityInstancesUIds.first())
            .one()
            .blockingGet()

        Log.d("RELATIONSHIPS_PARENT_TEI","$result")
//            .flatMap { tei ->
//                listOf(tei)
//            }.map { tei ->
//                transform(tei, program)
//            }.map {
//                val total = d2.eventModule().events()
//                    .byTrackedEntityInstanceUids(listOf(it.tei.uid()))
//                    .byStatus().eq(EventStatus.ACTIVE)
//                    .blockingCount()
//
//                Patient(
//                    uid = it.tei.uid(),
//                    name = getTeiAttrValue(it, 0),
//                    birthDate = getTeiAttrValue(it, 1),
//                    sex = getTeiAttrValue(it, 2),
//                    personInChargeName = getTeiAttrValue(it, 3),
//                    relationship = getTeiAttrValue(it, 4),
//                    phone = getTeiAttrValue(it, 5),
//                    scheduledAppointments = total
//                )
//            }
        return@withContext result

//        return@withContext (if (networkUtils.isOnline()) {
//            repository.offlineFirst().allowOnlineCache().eq(true)
//                .byProgram().eq(program)
//                .byTrackedEntities().eq(uid)
//                .one()
//                .blockingGet()
//        } else {
//            repository.offlineOnly().allowOnlineCache().eq(false)
//                .byProgram().eq(program)
//                .byTrackedEntities().eq(uid)
//                .one()
//                .blockingGet()
//        })
    }
}