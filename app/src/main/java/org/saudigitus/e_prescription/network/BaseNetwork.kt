package org.saudigitus.e_prescription.network

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import okhttp3.Credentials
import org.saudigitus.e_prescription.data.Result
import org.saudigitus.e_prescription.network.exception.NetworkException

abstract class BaseNetwork(
    open val context: Context,
    open val httpClient: HttpClient,
    open val networkUtil: NetworkUtils,
) {


    suspend inline fun <T> safeCall(
        crossinline block: suspend () -> T
    ): Result<T> {
        if (!networkUtil.isOnline()) {
            return Result.Error(NetworkException.NoInternet())
        }

        return try {
            Result.Success(block())
        } catch (e: ClientRequestException) {
            Result.Error(mapHttpException(e.response.status.value, e))
        } catch (e: ServerResponseException) {
            Result.Error(
                NetworkException.Api(
                    e.response.status.value,
                    e.message
                )
            )
        } catch (e: Exception) {
            Result.Error(NetworkException.Unknown(e.message))
        }
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

    suspend inline fun dhis2Login(
        route: String,
        username: String,
        password: String
    ): Result<Boolean> = safeCall {
        httpClient.get(route) {
            headers {
                val credentials = Credentials.basic(username, password)
                append(HttpHeaders.Authorization, credentials)
            }
        }.let { response ->
            response.status.value in listOf(200, 201, 304)
        }
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

    fun mapHttpException(code: Int, e: Throwable): NetworkException {
        return when (code) {
            401 -> NetworkException.Unauthorized()
            404 -> NetworkException.NotFound()
            else -> NetworkException.Api(code, e.message.orEmpty())
        }
    }
}

