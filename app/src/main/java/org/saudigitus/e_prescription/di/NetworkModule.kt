package org.saudigitus.e_prescription.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.jackson.jackson
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.network.CredentialProvider
import org.saudigitus.e_prescription.network.CredentialProviderImpl
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
    fun providesHttpClient(
        credentialProvider: CredentialProvider
    ): HttpClient {
        return HttpClient(CIO){
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
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = credentialProvider.getUsername(),
                            password = credentialProvider.getPassword()
                        )
                    }
                    sendWithoutRequest { true }
                }
            }
            defaultRequest {
                url(credentialProvider.getUrl())
            }

            engine {
                requestTimeout = 10000L
            }
        }
    }

    @Provides
    @Singleton
    fun providesNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}