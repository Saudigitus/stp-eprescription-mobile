package org.saudigitus.e_prescription.presentation.screens.prescriptions

import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.presentation.screens.prescriptions.model.BottomSheetState

data class PrescriptionUiState(
    val isLoading: Boolean = true,
    val displayErrors: Boolean = false,
    val isSaving: Boolean = false,
    val prescriptions: List<Prescription> = emptyList(),
    val errorState: BottomSheetState.ErrorState? = null,
    val saveState: BottomSheetState.SaveState? = null,
    val prescTei: TrackedEntityInstance? = null,
    val isSaved: Boolean = false
)
