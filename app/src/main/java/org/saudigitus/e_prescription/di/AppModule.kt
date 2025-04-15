package org.saudigitus.e_prescription.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.hisp.dhis.android.core.D2
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.data.local.PrescriptionRepository
import org.saudigitus.e_prescription.data.local.Sdk.d2
import org.saudigitus.e_prescription.data.local.repository.PreferenceProviderImpl
import org.saudigitus.e_prescription.data.local.repository.PrescriptionRepositoryImpl
import org.saudigitus.e_prescription.data.remote.SyncManagerRepository
import org.saudigitus.e_prescription.data.remote.UserManagerRepository
import org.saudigitus.e_prescription.data.remote.WorkManagerRepository
import org.saudigitus.e_prescription.data.remote.repository.SyncManagerRepositoryImpl
import org.saudigitus.e_prescription.data.remote.repository.UserManagerRepositoryImpl
import org.saudigitus.e_prescription.data.remote.repository.WorkManagerRepositoryImpl
import org.saudigitus.e_prescription.utils.NetworkUtils
import org.saudigitus.e_prescription.utils.ResourceManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesD2(@ApplicationContext context: Context): D2 = d2(context)

    @Provides
    @Singleton
    fun provideResourcesProvider(@ApplicationContext context: Context): ResourceManager =
        ResourceManager(context)

    @Provides
    @Singleton
    fun providesUserManagerImpl(
        d2: D2,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        resourceManager: ResourceManager
    ): UserManagerRepository = UserManagerRepositoryImpl(d2, ioDispatcher, resourceManager)

    @Provides
    @Singleton
    fun providesWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun providesWorkManagerRepositoryImpl(workManager: WorkManager): WorkManagerRepository =
        WorkManagerRepositoryImpl(workManager)

    @Provides
    @Singleton
    fun providesPreferenceProvider(@ApplicationContext context: Context): PreferenceProvider =
        PreferenceProviderImpl(context)

    @Provides
    @Singleton
    fun providesSyncManagerRepositoryImpl(
        d2: D2,
        workManagerRepository: WorkManagerRepository,
        preferenceProvider: PreferenceProvider
    ): SyncManagerRepository = SyncManagerRepositoryImpl(d2, workManagerRepository, preferenceProvider)

    @Provides
    @Singleton
    fun providesNetworkUtils(@ApplicationContext context: Context) = NetworkUtils(context)

    @Provides
    @Singleton
    fun providePrescriptionRepository(d2: D2): PrescriptionRepository = PrescriptionRepositoryImpl(d2)
}