package org.saudigitus.e_prescription.data.local.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hisp.dhis.android.core.D2
import org.saudigitus.e_prescription.data.local.PrescriptionRepository
import org.saudigitus.e_prescription.data.model.Patient
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.data.model.response.DataElement
import org.saudigitus.e_prescription.data.model.response.OptionResponse
import org.saudigitus.e_prescription.data.model.response.TrackedEntityInstanceResponse
import org.saudigitus.e_prescription.network.URLMapping.dataElementUrl
import org.saudigitus.e_prescription.network.URLMapping.optionsUrl
import org.saudigitus.e_prescription.network.URLMapping.teiAttributesUrl
import org.saudigitus.e_prescription.network.URLMapping.teiEventsUrl
import org.saudigitus.e_prescription.utils.AttributesHelper
import org.saudigitus.e_prescription.utils.UIDMapping
import org.saudigitus.e_prescription.utils.UIDMapping.attributes
import org.saudigitus.e_prescription.utils.UIDMapping.dataElements
import org.saudigitus.e_prescription.utils.getByAttr


class PrescriptionRepositoryImpl(
    private val d2: D2,
    private val attributesHelper: AttributesHelper
): PrescriptionRepository {
    override suspend fun savePrescription(
        event: String,
        dataElement: String,
        value: String
    ) = withContext(Dispatchers.IO) {
        d2.trackedEntityModule().trackedEntityDataValues()
            .value(event, dataElement)
            .blockingSet(value)

        d2.eventModule().events().uid(event)

        d2.eventModule().events().blockingUpload()
    }

    override suspend fun getPrescriptions(
        tei: String,
        program: String,
        stage: String,
    ) = withContext(Dispatchers.IO) {

        val response = d2.httpServiceClient().get<TrackedEntityInstanceResponse> {
            url(teiEventsUrl(tei, program))
        }

        val events = response.trackedEntityInstances.flatMap { it.enrollments }
            .flatMap { it.events }
            .filter { it.programStage == stage }

        val eventWithDataValues = events.map { Pair(it.event, it.dataValues) }

        eventWithDataValues.flatMap { eventWithDataValues ->

            val dataValues = eventWithDataValues.second.filter { it.dataElement in dataElements() }

            dataValues.map { dataValue ->
                val dataElement = d2.httpServiceClient().get<DataElement> {
                    url(dataElementUrl(dataValue.dataElement))
                }

                val name = if (dataElement.id == UIDMapping.DATA_ELEMENT_NAME) {
                    d2.httpServiceClient().get<OptionResponse> {
                        url(optionsUrl(dataValue.value))
                    }.options.find { it.code == dataValue.value }
                        ?.name
                } else null

                val posology = if (dataValue.dataElement == UIDMapping.DATA_ELEMENT_POSOLOGY) {
                    dataValue.value
                } else {
                    "-"
                }

                val requestedQtd = if (dataValue.dataElement == UIDMapping.DATA_ELEMENT_QTD_REQ) {
                    dataValue.value
                } else {
                    "0"
                }

                Prescription(
                    uid = eventWithDataValues.first,
                    name = name.orEmpty(),
                    posology = posology,
                    requestedQtd = requestedQtd.toInt(),
                    isCompleted = true
                )
            }
        }


        /*d2.eventsWithTrackedDataValues(tei, program, stage)
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
            }*/
    }

    override suspend fun getPatient(
        uid: String,
        program: String
    ) : Patient? = withContext(Dispatchers.IO) {
        return@withContext try {
            /*d2.trackedEntityModule().trackedEntityInstanceDownloader()
                .byUid().`in`(uid)
                .byProgramUid(program)
                .blockingDownload()

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

            val teiUid = trackedEntityInstancesUIds.first()*/

            val response = d2.httpServiceClient().get<TrackedEntityInstanceResponse> {
                url(teiAttributesUrl(uid, program))
            }

            val attributes = response.trackedEntityInstances.flatMap { it.attributes }
                .filter { it.attribute in attributes()  }

            Patient(
                uid = uid,
                name = attributes.getByAttr(UIDMapping.NAME_ATTR),
                surname = attributes.getByAttr(UIDMapping.SURNAME_ATTR),
                birthdate = attributes.getByAttr(UIDMapping.BIRTHDATE_ATTR),
                residence = attributes.getByAttr(UIDMapping.RESIDENCE_ATTR),
                gender = attributes.getByAttr(UIDMapping.GENDER_ATTR),
                processNumber = attributes.getByAttr(UIDMapping.PROCESS_NUMBER_ATTR),
            )
        } catch (_: Exception) {
            null
        }
    }
}