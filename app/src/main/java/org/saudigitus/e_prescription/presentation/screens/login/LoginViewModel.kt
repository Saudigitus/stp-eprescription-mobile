package org.saudigitus.e_prescription.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.saudigitus.e_prescription.data.Result
import org.saudigitus.e_prescription.data.remote.repository.UserManagerRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val userManager: UserManagerRepository,
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState(serverUrl = "https://"))
    val loginUiState = _loginUiState.asStateFlow()

    private val _loginResult = MutableSharedFlow<LoginResult>()
    val loginResult = _loginResult.asSharedFlow()

    fun serverUrl(serverUrl: String) {
        _loginUiState.update {
            it.copy(serverUrl = serverUrl)
        }
    }

    fun username(username: String) {
        _loginUiState.update {
            it.copy(username = username)
        }
    }

    fun password(password: String) {
        _loginUiState.update {
            it.copy(password = password)
        }
    }

    fun passwordVisibility(visibility: Boolean) {
        _loginUiState.update {
            it.copy(passwordVisible = visibility)
        }
    }

    private fun progressLoading(isLoading: Boolean = false) {
        _loginUiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun login() {
        viewModelScope.launch {
            progressLoading(true)

            val result = userManager.login(
                loginUiState.value.serverUrl,
                loginUiState.value.username,
                loginUiState.value.password
            )

            when (result) {
                is Result.Success -> {
                    progressLoading(false)
                    _loginResult.emit(LoginResult(success = result.data))
                }

                is Result.Error -> {
                    progressLoading(false)
                    _loginResult.emit(LoginResult(error = result.exception.message))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userManager.logout()
        }
    }
}