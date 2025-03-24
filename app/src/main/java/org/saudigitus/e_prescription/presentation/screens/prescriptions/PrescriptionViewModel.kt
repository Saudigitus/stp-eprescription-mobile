package org.saudigitus.e_prescription.presentation.screens.prescriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.data.model.MedicineIndicators
import org.saudigitus.e_prescription.presentation.screens.prescriptions.model.BottomSheetState
import org.saudigitus.e_prescription.presentation.screens.prescriptions.model.InputFieldModel
import org.saudigitus.e_prescription.utils.toPrescriptionError
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel
@Inject constructor() : ViewModel() {
    private val viewModelState = MutableStateFlow(
        PrescriptionUiState(prescriptions = cardState),
    )

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value,
        )


    private val _cacheGivenMedicines = MutableStateFlow<List<InputFieldModel>>(emptyList())
    val cacheGivenMedicines: StateFlow<List<InputFieldModel>> = _cacheGivenMedicines

    fun onUiEvent(prescriptionUiEvent: PrescriptionUiEvent) {
        when (prescriptionUiEvent) {
            is PrescriptionUiEvent.OnPrescriptionValueChange -> {
                val cache = mutableListOf<InputFieldModel>()
                cache.addAll(cacheGivenMedicines.value)

                val index = cache.indexOfFirst { it.key == prescriptionUiEvent.prescription.uid }

                if (index >= 0) {
                    cache.removeAt(index)
                    cache.add(
                        index,
                        InputFieldModel(
                            key = prescriptionUiEvent.prescription.uid,
                            value = prescriptionUiEvent.value,
                            conditionalValue = prescriptionUiEvent.prescription.requestedQtd.toString()
                        )
                    )
                } else {
                    cache.add(
                        InputFieldModel(
                            key = prescriptionUiEvent.prescription.uid,
                            value = prescriptionUiEvent.value,
                            conditionalValue = prescriptionUiEvent.prescription.requestedQtd.toString()
                        )
                    )
                }

                _cacheGivenMedicines.value = cache
            }
            is PrescriptionUiEvent.OnTempSave -> {
                viewModelScope.launch {
                    val uids = failedUids()

                    if (uids.isNotEmpty()) {
                        viewModelState.update {
                            it.copy(displayErrors = true)
                        }

                        getFailedToProcess(uids)
                    } else {
                        viewModelState.update {
                            it.copy(
                                isSaving = true,
                                saveState = BottomSheetState.SaveState(
                                    isLoading = false,
                                    indicators = checkMedicines()
                                )
                            )
                        }
                    }
                }
            }
            is PrescriptionUiEvent.OnSave -> {

            }
            is PrescriptionUiEvent.CloseErrorBottomSheet -> {
                viewModelState.update {
                    it.copy(displayErrors = false)
                }
            }
            is PrescriptionUiEvent.CloseSaveBottomSheet -> {
                viewModelState.update {
                    it.copy(isSaving = false)
                }
            }
            else -> {}
        }
    }

    private fun failedUids() = cacheGivenMedicines.value.filter { it.hasError() }
        .map { it.key }

    private fun getFailedToProcess(uids: List<String>) {
        viewModelScope.launch {
            val prescriptions = viewModelState.value.prescriptions.filter { it.uid in uids }

            val errors = prescriptions.mapNotNull { prescription ->
                val data = cacheGivenMedicines.value.find { it.key == prescription.uid }

                if (data != null) {
                    prescription.toPrescriptionError(data.value.toInt())
                } else null
            }

            viewModelState.update {
                it.copy(
                    errorState = BottomSheetState.ErrorState(
                        isLoading = false,
                        prescriptions = errors
                    )
                )
            }
        }
    }

    private fun checkMedicines(): MedicineIndicators {
        var completedCount = 0
        var incompleteCount = 0
        var zeroCount = 0

        cacheGivenMedicines.value.forEach { (_, value, conditionalValue) ->
            val givenAmount = value.toIntOrNull() ?: 0
            val requiredAmount = conditionalValue?.toIntOrNull() ?: 0

            when {
                value.isEmpty() -> zeroCount++
                givenAmount == requiredAmount -> completedCount++
                givenAmount > requiredAmount -> incompleteCount++
            }
        }

        return MedicineIndicators(
            Pair(R.string.completed_medicine, completedCount),
            Pair(R.string.incomplete_medicine, incompleteCount),
            Pair(R.string.non_existent_medicine, zeroCount)
        )
    }
}