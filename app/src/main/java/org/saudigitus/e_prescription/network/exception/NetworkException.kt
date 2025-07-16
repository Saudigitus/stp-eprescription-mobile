package org.saudigitus.e_prescription.network.exception


sealed class NetworkException(message: String) : Exception(message) {
    data object NoInternet : NetworkException("Not connected to internet") {
        private fun readResolve(): Any = NoInternet
    }

    data object InvalidToken : NetworkException("Invalid or expired token") {
        private fun readResolve(): Any = InvalidToken
    }
}