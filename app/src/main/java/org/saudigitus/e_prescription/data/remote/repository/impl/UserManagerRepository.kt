package org.saudigitus.e_prescription.data.remote.repository.impl


import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.saudigitus.e_prescription.data.Result
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.data.remote.repository.UserManagerRepository
import org.saudigitus.e_prescription.network.BaseNetwork
import org.saudigitus.e_prescription.network.HttpClientHelper
import org.saudigitus.e_prescription.network.NetworkUtils
import org.saudigitus.e_prescription.network.URLMapping.meUrl
import org.saudigitus.e_prescription.network.exception.NetworkException
import javax.inject.Inject

class UserManagerRepositoryImpl
@Inject constructor(
    override val context: Context,
    override val networkUtil: NetworkUtils,
    httpClientHelper: HttpClientHelper,
    private val preferenceProvider: PreferenceProvider,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
): BaseNetwork(context, httpClientHelper.httpClient(), networkUtil), UserManagerRepository {
    override suspend fun login(
        server: String,
        username: String,
        password: String
    ) = withContext(ioDispatcher) {
        try {
            val baseUrl = if (server.endsWith("/")) {
                server.trim()
            } else "$server/"

            when (val response = dhis2Login(meUrl(baseUrl), username, password)) {
                is Result.Success -> {
                    preferenceProvider.setValue("URL", baseUrl)
                    preferenceProvider.setValue("USERNAME", username)
                    preferenceProvider.setValue("PASSWORD", password)

                    Result.Success(true)
                }

                is Result.Error -> {
                    Result.Error(response.exception)
                }
            }
        } catch (e: Exception) {
            return@withContext Result.Error(NetworkException.Unknown(e.message))
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