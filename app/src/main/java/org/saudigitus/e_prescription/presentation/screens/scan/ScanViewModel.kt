package org.saudigitus.e_prescription.presentation.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.saudigitus.e_prescription.data.remote.SyncManagerRepository
import org.saudigitus.e_prescription.data.remote.UserManagerRepository
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val syncRepository: SyncManagerRepository,
    private val loginRepository: UserManagerRepository
): ViewModel() {
    private val viewModelState = MutableStateFlow(
        ScanUiState(),
    )

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value,
        )


    fun onUiEvent(scanUiEvent: ScanUiEvent) {
        when (scanUiEvent) {
            is ScanUiEvent.Scan -> {
                viewModelState.update {
                    it.copy(scanResult = scanUiEvent.scanResult)
                }
            }
            is ScanUiEvent.SyncData -> {
                viewModelScope.launch {
                    syncRepository.syncDataWithTrigger()
                }
            }
            is ScanUiEvent.SyncMetaData -> {
                viewModelScope.launch {
                    syncRepository.syncMetaDataWithTrigger()
                }
            }
            is ScanUiEvent.Logout -> {
                viewModelScope.launch {
                    loginRepository.logout()
                }
            }
            else -> {}
        }
    }
}