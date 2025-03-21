package org.saudigitus.e_prescription.presentation.screens.scan

sealed class ScanUiEvent {
    data class Scan(val scanResult: String): ScanUiEvent()
    data class NavTo(val route: String): ScanUiEvent()
}