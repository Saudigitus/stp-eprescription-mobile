package org.saudigitus.e_prescription.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.jackson.jackson
import okhttp3.OkHttpClient
import javax.inject.Inject

class HttpClientHelper @Inject constructor(
    private val credentialProvider: CredentialProvider
) {
    fun httpClient(): HttpClient {
        return HttpClient(OkHttp){
            install(ContentNegotiation) {
                jackson()
            }
            install(HttpRequestRetry) {
                retryOnException(5, true)
                exponentialDelay()
            }
            defaultRequest {
                url(credentialProvider.getUrl())
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            engine {
                preconfigured = OkHttpClient.Builder()
                    .addInterceptor(basicAuthInterceptor(credentialProvider))
                    .build()
            }
        }
    }
}