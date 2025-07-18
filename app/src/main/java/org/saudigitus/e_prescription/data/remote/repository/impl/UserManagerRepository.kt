package org.saudigitus.e_prescription.data.remote.repository.impl


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.saudigitus.e_prescription.data.Result
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.data.remote.repository.UserManagerRepository
import org.saudigitus.e_prescription.network.BaseNetwork
import org.saudigitus.e_prescription.network.HttpClientHelper
import org.saudigitus.e_prescription.network.NetworkUtils
import org.saudigitus.e_prescription.network.URLMapping.resourcesUrl
import javax.inject.Inject

class UserManagerRepositoryImpl
@Inject constructor(
    override val networkUtil: NetworkUtils,
    httpClientHelper: HttpClientHelper,
    private val preferenceProvider: PreferenceProvider,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
): BaseNetwork(httpClientHelper.httpClient(), networkUtil), UserManagerRepository {
    override suspend fun login(
        server: String,
        username: String,
        password: String
    ) = withContext(ioDispatcher) {
        try {
            val isLogged = dhis2Login(resourcesUrl(server), username, password)
                .getOrElse { false }

            if (isLogged) {
                preferenceProvider.setValue("URL", server)
                preferenceProvider.setValue("USERNAME", username)
                preferenceProvider.setValue("PASSWORD", password)
            }

            return@withContext Result.Success(isLogged)
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun isLoggedIn() = withContext(ioDispatcher) {
        Result.Success(
            preferenceProvider.getString("URL")?.isNotEmpty() == true &&
                    preferenceProvider.getString("USERNAME")?.isNotEmpty() == true &&
                    preferenceProvider.getString("PASSWORD")?.isNotEmpty() == true
        )
    }

    override suspend fun userName() = withContext(ioDispatcher) {
        Result.Success("e-prescription")
    }

    override suspend fun logout() {
        withContext(ioDispatcher) {
            preferenceProvider.clear()
        }
    }
}