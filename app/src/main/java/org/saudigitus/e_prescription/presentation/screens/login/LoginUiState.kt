package org.saudigitus.e_prescription.presentation.screens.login

import android.util.Patterns

data class LoginUiState(
    val serverUrl: String = "",
    val username: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false
){
    fun isServerUrlValid(): Boolean =  Patterns.WEB_URL.matcher(serverUrl).matches()

    fun isUserNameValid(): Boolean = username.trim().isNotEmpty()

    fun isPasswordValid(): Boolean = password.trim().isNotEmpty()

    fun enableLoginBtn(): Boolean = isServerUrlValid() && isUserNameValid() && isPasswordValid()
}