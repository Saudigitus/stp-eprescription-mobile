package org.saudigitus.e_prescription.presentation.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(): ViewModel() {
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
            else -> {}
        }
    }
}