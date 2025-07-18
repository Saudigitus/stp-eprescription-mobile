package org.saudigitus.e_prescription.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.network.CredentialProvider
import org.saudigitus.e_prescription.network.CredentialProviderImpl
import org.saudigitus.e_prescription.network.HttpClientHelper
import org.saudigitus.e_prescription.network.NetworkUtils
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCredentialProvider(
        preferenceProvider: PreferenceProvider
    ): CredentialProvider = CredentialProviderImpl(preferenceProvider)

    @Provides
    @Singleton
    fun provideHttpClientHelper(
        credentialProvider: CredentialProvider
    ): HttpClientHelper = HttpClientHelper(
        credentialProvider
    )

    @Provides
    @Singleton
    fun providesNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}