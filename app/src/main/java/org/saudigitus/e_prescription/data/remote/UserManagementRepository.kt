package org.saudigitus.e_prescription.data.remote

import org.hisp.dhis.android.core.user.User
import org.saudigitus.e_prescription.data.Result

interface UserManagerRepository {

    suspend fun login(server: String, username: String, password: String): Result<User>

    suspend fun isLoggedIn(): Result<Boolean>

    suspend fun userName(): Result<String?>

    suspend fun logout()
}