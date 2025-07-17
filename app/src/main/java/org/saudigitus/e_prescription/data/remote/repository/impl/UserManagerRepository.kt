package org.saudigitus.e_prescription.data.remote.repository.impl


import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.saudigitus.e_prescription.data.Result
import org.saudigitus.e_prescription.data.remote.repository.UserManagerRepository
import org.saudigitus.e_prescription.network.BaseNetwork
import org.saudigitus.e_prescription.network.NetworkUtils
import javax.inject.Inject

class UserManagerRepositoryImpl
@Inject constructor(
    override val httpClient: HttpClient,
    override val networkUtil: NetworkUtils,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
): BaseNetwork(httpClient, networkUtil), UserManagerRepository {
    override suspend fun login(
        server: String,
        username: String,
        password: String
    ) = withContext(ioDispatcher) {
        try {
            //TODO: Implement login logic

            return@withContext Result.Success(true)
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun isLoggedIn() = withContext(ioDispatcher) {
        Result.Success(true)
    }

    override suspend fun userName() = withContext(ioDispatcher) {
        Result.Success("")
    }

    override suspend fun logout() {
        withContext(ioDispatcher) {

        }
    }
}