package org.saudigitus.e_prescription.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.data.local.repository.PreferenceProviderImpl
import org.saudigitus.e_prescription.data.remote.repository.PrescriptionRepository
import org.saudigitus.e_prescription.data.remote.repository.UserManagerRepository
import org.saudigitus.e_prescription.data.remote.repository.impl.PrescriptionRepositoryImpl
import org.saudigitus.e_prescription.data.remote.repository.impl.UserManagerRepositoryImpl
import org.saudigitus.e_prescription.network.HttpClientHelper
import org.saudigitus.e_prescription.network.NetworkUtils
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesUserManagerImpl(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        httpClientHelper: HttpClientHelper,
        networkUtils: NetworkUtils,
        preferenceProvider: PreferenceProvider
    ): UserManagerRepository = UserManagerRepositoryImpl(
        networkUtils,
        httpClientHelper,
        preferenceProvider,
        ioDispatcher
    )

    @Provides
    @Singleton
    fun providesPreferenceProvider(@ApplicationContext context: Context): PreferenceProvider =
        PreferenceProviderImpl(context)

    @Provides
    @Singleton
    fun providePrescriptionRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        httpClientHelper: HttpClientHelper,
        networkUtils: NetworkUtils
    ): PrescriptionRepository = PrescriptionRepositoryImpl(networkUtils, httpClientHelper,  ioDispatcher)
}