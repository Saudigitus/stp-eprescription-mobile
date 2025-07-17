package org.saudigitus.e_prescription.data.remote.repository.impl

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.saudigitus.e_prescription.data.model.Patient
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.data.model.response.OptionResponse
import org.saudigitus.e_prescription.data.model.response.TrackedEntityInstanceResponse
import org.saudigitus.e_prescription.data.remote.repository.PrescriptionRepository
import org.saudigitus.e_prescription.network.BaseNetwork
import org.saudigitus.e_prescription.network.NetworkUtils
import org.saudigitus.e_prescription.network.URLMapping.optionsUrl
import org.saudigitus.e_prescription.network.URLMapping.teiAttributesUrl
import org.saudigitus.e_prescription.network.URLMapping.teiEventsUrl
import org.saudigitus.e_prescription.network.URLMapping.teiRelationshipUrl
import org.saudigitus.e_prescription.utils.UIDMapping
import org.saudigitus.e_prescription.utils.UIDMapping.attributes
import org.saudigitus.e_prescription.utils.getByAttr

class PrescriptionRepositoryImpl(
    override val httpClient: HttpClient,
    override val networkUtil: NetworkUtils,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): BaseNetwork(httpClient, networkUtil), PrescriptionRepository {
    override suspend fun savePrescription(
        event: String,
        dataElement: String,
        value: String
    ) = withContext(ioDispatcher) {
        //TODO: Implement logic to save prescription
    }

    override suspend fun getPrescriptions(
        tei: String,
        program: String,
        stage: String,
    ): List<Prescription> = withContext(ioDispatcher) {
        val events = get<TrackedEntityInstanceResponse>(teiEventsUrl(tei, program))
            .getOrNull()
            ?.trackedEntityInstances
            ?.flatMap { it.enrollments }
            ?.flatMap { it.events }
            ?.filter { it.programStage == stage }
            ?: return@withContext emptyList()

        val eventDataMap = events.associateBy({ it.event }, { it.dataValues })

        return@withContext eventDataMap.keys.map { eventId ->
            val dataValues = eventDataMap[eventId] ?: emptyList()

            val optionCode = dataValues.first { it.dataElement == UIDMapping.DATA_ELEMENT_NAME }.value
            val posology = dataValues.first { it.dataElement == UIDMapping.DATA_ELEMENT_POSOLOGY }.value
            val requestedQtd = dataValues.first { it.dataElement == UIDMapping.DATA_ELEMENT_QTD_REQ }
                .value.toIntOrNull() ?: 0

            val name = get<OptionResponse>(optionsUrl(optionCode))
                .getOrNull()
                ?.options
                ?.find { it.code == optionCode }
                ?.name.orEmpty()

            Prescription(
                uid = eventId,
                name = name,
                posology = posology,
                requestedQtd = requestedQtd,
                isCompleted = true
            )
        }
    }

    override suspend fun getPatient(
        uid: String,
        program: String,
        relationshipType: String
    ): Patient? = withContext(ioDispatcher) {
        val relationship = async {
            get<TrackedEntityInstanceResponse>(teiRelationshipUrl(uid, UIDMapping.PROGRAM))
                .getOrNull()
                ?.trackedEntityInstances
                ?.flatMap { it.relationships }
                ?.find { it.relationshipType == relationshipType }
        }.await()

        val relatedTei = relationship?.from?.trackedEntityInstance?.trackedEntityInstance.orEmpty()
        if (relatedTei.isEmpty()) return@withContext null

        val response = get<TrackedEntityInstanceResponse>(teiAttributesUrl(relatedTei, program))
        val teiData = response.getOrNull() ?: return@withContext null

        val attributes = teiData.trackedEntityInstances
            .flatMap { it.attributes }
            .filter { it.attribute in attributes() }

        return@withContext Patient(
            uid = uid,
            name = attributes.getByAttr(UIDMapping.NAME_ATTR),
            surname = attributes.getByAttr(UIDMapping.SURNAME_ATTR),
            birthdate = attributes.getByAttr(UIDMapping.BIRTHDATE_ATTR),
            residence = attributes.getByAttr(UIDMapping.RESIDENCE_ATTR),
            gender = attributes.getByAttr(UIDMapping.GENDER_ATTR),
            processNumber = attributes.getByAttr(UIDMapping.PROCESS_NUMBER_ATTR),
        )
    }
}