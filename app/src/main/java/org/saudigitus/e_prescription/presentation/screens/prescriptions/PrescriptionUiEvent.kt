package org.saudigitus.e_prescription.presentation.screens.prescriptions

import org.saudigitus.e_prescription.data.model.Prescription

sealed class PrescriptionUiEvent {
    data class OnPrescriptionValueChange(val prescription: Prescription, val value: String): PrescriptionUiEvent()
    data object OnTempSave : PrescriptionUiEvent()
    data object OnSave : PrescriptionUiEvent()
    data object OnBack: PrescriptionUiEvent()
    data object CloseErrorBottomSheet: PrescriptionUiEvent()
    data object CloseSaveBottomSheet: PrescriptionUiEvent()
}