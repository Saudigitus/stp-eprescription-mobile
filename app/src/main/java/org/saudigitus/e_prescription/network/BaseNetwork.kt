package org.saudigitus.e_prescription.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import okhttp3.Credentials
import org.saudigitus.e_prescription.network.exception.NetworkException

abstract class BaseNetwork(
    open val httpClient: HttpClient,
    open val networkUtil: NetworkUtils,
) {

    suspend inline fun <T> safeCall(
        crossinline block: suspend () -> T
    ): Result<T> {
        if (!networkUtil.isOnline()) {
            return Result.failure(NetworkException.NoInternet)
        }

        return runCatching { block() }
    }

    /**
     * This is HTTP post method
     * @param route is the API endpoint
     * @param body is the request body
     * @return Result<T> is the response body
     */
    suspend inline fun <reified T, reified E> post(route: String, body: E): Result<T> =
        safeCall {
            httpClient.post(route) {
                setBody(body)
            }.body()
        }

    /**
     * This is HTTP get method
     * @param route is the API endpoint
     * @return Result<T> is the response body
     */
    suspend inline fun <reified T> get(route: String): Result<T> =
        safeCall {
            httpClient.get(route).body()
        }

    suspend inline fun dhis2Login(route: String, username: String, password: String): Result<Boolean> =
        safeCall {
            headers {
                val credentials = Credentials.basic(username, password)
                append(HttpHeaders.Authorization, credentials)
            }
            httpClient.get(route).status.value == 200 || httpClient.get(route).status.value == 201
        }

    /**
     * This is HTTP put method
     * @param route is the API endpoint
     * @param body is the request body
     * @return Result<T> is the response body
     */
    suspend inline fun <reified T, reified E> put(route: String, body: E): Result<Pair<Int, T>> =
        safeCall {
            val response = httpClient.put(route) {
                setBody(body)
            }

            Pair(response.status.value, response.body<T>())
        }
}
