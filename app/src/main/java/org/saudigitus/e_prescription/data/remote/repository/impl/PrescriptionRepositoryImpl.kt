package org.saudigitus.e_prescription.data.remote.repository.impl

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.saudigitus.e_prescription.data.model.Patient
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.data.remote.repository.PrescriptionRepository
import org.saudigitus.e_prescription.network.BaseNetwork
import org.saudigitus.e_prescription.network.NetworkUtils

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
    ) = withContext(ioDispatcher) {
        emptyList<Prescription>()
    }

    override suspend fun getPatient(
        uid: String,
        program: String
    ) : Patient? = withContext(ioDispatcher) {
        null
    }
}