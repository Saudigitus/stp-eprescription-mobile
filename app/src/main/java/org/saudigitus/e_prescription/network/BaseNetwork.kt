package org.saudigitus.e_prescription.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
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

    /**
     * This is HTTP put method
     * @param route is the API endpoint
     * @param body is the request body
     * @return Result<T> is the response body
     */
    suspend inline fun <reified T, reified E> put(route: String, body: E): Result<T> =
        safeCall {
            httpClient.put(route) {
                setBody(body)
            }.body()
        }
}
