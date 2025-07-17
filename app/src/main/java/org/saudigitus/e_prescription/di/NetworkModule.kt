package org.saudigitus.e_prescription.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.jackson.jackson
import okhttp3.OkHttpClient
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.network.CredentialProvider
import org.saudigitus.e_prescription.network.CredentialProviderImpl
import org.saudigitus.e_prescription.network.NetworkUtils
import org.saudigitus.e_prescription.network.basicAuthInterceptor
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
    fun providesHttpClient(
        credentialProvider: CredentialProvider
    ): HttpClient {
        return HttpClient(OkHttp){
            install(DefaultRequest){
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                jackson()
            }
            install(HttpRequestRetry) {
                retryOnException(5, true)
                exponentialDelay()
            }
            defaultRequest {
                url("https://dhis2.gov.st/tracker/")
            }

            engine {
                preconfigured = OkHttpClient.Builder()
                    .addInterceptor(basicAuthInterceptor)
                    .build()
            }
        }
    }

    @Provides
    @Singleton
    fun providesNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}