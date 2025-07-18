package org.saudigitus.e_prescription.network

import android.util.Log
import okhttp3.Credentials
import okhttp3.Interceptor
import org.saudigitus.e_prescription.utils.Constants.AUTH_HEADER

fun basicAuthInterceptor(credentialProvider: CredentialProvider) =  Interceptor { chain ->
    val credentials = Credentials.basic(
        credentialProvider.getUsername(),
        credentialProvider.getPassword()
    )

    Log.e("basicAuthInterceptor", "basicAuthInterceptor: ${credentialProvider.getUsername()} -> ${credentialProvider.getPassword()}")

    Log.e("basicAuthInterceptor", "Request Auth Header: $credentials")

    val request = chain.request().newBuilder()
        .addHeader(AUTH_HEADER, credentials)
        .build()

    chain.proceed(request)
}