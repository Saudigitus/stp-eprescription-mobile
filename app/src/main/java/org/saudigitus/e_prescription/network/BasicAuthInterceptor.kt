package org.saudigitus.e_prescription.network

import io.ktor.http.HttpHeaders
import okhttp3.Credentials
import okhttp3.Interceptor

fun basicAuthInterceptor(credentialProvider: CredentialProvider) =  Interceptor { chain ->
    val credentials = Credentials.basic(
        credentialProvider.getUsername(),
        credentialProvider.getPassword()
    )

    val request = chain.request().newBuilder()
        .addHeader(HttpHeaders.Authorization, credentials)
        .build()

    chain.proceed(request)
}