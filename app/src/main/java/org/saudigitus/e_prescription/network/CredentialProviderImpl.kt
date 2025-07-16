package org.saudigitus.e_prescription.network

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialProviderImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : CredentialProvider {
    override fun getUrl(): String {
        return sharedPrefs.getString("URL", URLMapping.BASE_URL).orEmpty()
    }

    override fun getUsername(): String {
        return sharedPrefs.getString("USERNAME", "").orEmpty()
    }

    override fun getPassword(): String {
        return sharedPrefs.getString("PASSWORD", "").orEmpty()
    }
}
