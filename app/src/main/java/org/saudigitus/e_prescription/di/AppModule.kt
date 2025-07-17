package org.saudigitus.e_prescription.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.data.local.repository.PreferenceProviderImpl
import org.saudigitus.e_prescription.data.remote.repository.PrescriptionRepository
import org.saudigitus.e_prescription.data.remote.repository.UserManagerRepository
import org.saudigitus.e_prescription.data.remote.repository.impl.PrescriptionRepositoryImpl
import org.saudigitus.e_prescription.data.remote.repository.impl.UserManagerRepositoryImpl
import org.saudigitus.e_prescription.network.NetworkUtils
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesUserManagerImpl(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        httpClient: HttpClient,
        networkUtils: NetworkUtils
    ): UserManagerRepository = UserManagerRepositoryImpl(httpClient, networkUtils, ioDispatcher)


    @Provides
    @Singleton
    fun providesPreferenceProvider(@ApplicationContext context: Context): PreferenceProvider =
        PreferenceProviderImpl(context)

    @Provides
    @Singleton
    fun providePrescriptionRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        httpClient: HttpClient,
        networkUtils: NetworkUtils
    ): PrescriptionRepository = PrescriptionRepositoryImpl(httpClient, networkUtils, ioDispatcher)
}