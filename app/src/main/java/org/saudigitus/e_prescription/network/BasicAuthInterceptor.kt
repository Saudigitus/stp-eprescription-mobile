package org.saudigitus.e_prescription.network

import okhttp3.Credentials
import okhttp3.Interceptor

val basicAuthInterceptor =  Interceptor { chain ->
    val AUTH_HEADER = "Authorization"
    val username = "something"
    val password = "password"

    val credentials = Credentials.basic(username, password)

    val request = chain.request().newBuilder()
        .addHeader(AUTH_HEADER, credentials)
        .build()

    chain.proceed(request)
}