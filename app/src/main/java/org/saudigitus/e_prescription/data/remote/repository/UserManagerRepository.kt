package org.saudigitus.e_prescription.data.remote.repository


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hisp.dhis.android.core.D2
import org.saudigitus.e_prescription.data.Result
import org.saudigitus.e_prescription.data.remote.UserManagerRepository
import org.saudigitus.e_prescription.utils.ResourceManager
import javax.inject.Inject

class UserManagerRepositoryImpl
@Inject constructor(
    private val d2: D2,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val resourceManager: ResourceManager
): UserManagerRepository {
    override suspend fun login(
        server: String,
        username: String,
        password: String
    ) = withContext(ioDispatcher) {
        try {
            val user = d2.userModule().blockingLogIn(
                username,
                password,
                server
            )

            return@withContext Result.Success(user)
        } catch (e: Exception) {
            return@withContext Result.Error(Exception(resourceManager.getD2ErrorMessage(e)?.let {
                resourceManager
                    .getString(it)
            }))
        }
    }

    override suspend fun isLoggedIn() = withContext(ioDispatcher) {
        Result.Success(d2.userModule().blockingIsLogged())
    }

    override suspend fun userName() = withContext(ioDispatcher) {
        Result.Success(d2.userModule().user().blockingGet()?.displayName())
    }

    override suspend fun logout() {
        withContext(ioDispatcher) {
            d2.userModule().blockingLogOut()
        }
    }
}