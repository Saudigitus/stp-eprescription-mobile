package org.saudigitus.e_prescription.network.exception


sealed class NetworkException(
    override val message: String
) : Throwable(message) {

    class NoInternet : NetworkException(
        "No internet connection. Please check your network and try again."
    ) {
        override fun toString(): String {
            return this.message
        }
    }

    class Unauthorized : NetworkException(
        "Incorrect username or password."
    ) {
        override fun toString(): String {
            return this.message
        }
    }

    class NotFound : NetworkException(
        "The requested resource was not found."
    ) {
        override fun toString(): String {
            return this.message
        }
    }

    class Unknown(val error: String? = null) : NetworkException(
        error ?: "Something went wrong. Please try again."
    ) {
        override fun toString(): String {
            return this.message
        }
    }

    data class Api(
        val code: Int,
        override val message: String
    ) : NetworkException(message) {
        override fun toString(): String {
            return "Request Status: $code\nMessage: ${this.message}."
        }
    }

}