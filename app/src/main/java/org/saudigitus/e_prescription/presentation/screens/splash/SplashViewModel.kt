package org.saudigitus.e_prescription.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.saudigitus.e_prescription.data.next
import org.saudigitus.e_prescription.data.remote.UserManagerRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    private val userManager: UserManagerRepository
): ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            val result = userManager.isLoggedIn()

            delay(3000)

            _isLoggedIn.value = result.next
        }
    }
}