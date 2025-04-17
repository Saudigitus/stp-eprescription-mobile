package org.saudigitus.e_prescription.presentation.screens.prescriptions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.data.local.PrescriptionRepository
import org.saudigitus.e_prescription.data.model.MedicineIndicators
import org.saudigitus.e_prescription.presentation.screens.prescriptions.model.BottomSheetState
import org.saudigitus.e_prescription.presentation.screens.prescriptions.model.InputFieldModel
import org.saudigitus.e_prescription.utils.UIDMapping
import org.saudigitus.e_prescription.utils.toPrescriptionError
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel
@Inject constructor(
    private val repository: PrescriptionRepository
) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        PrescriptionUiState(),
    )

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value,
        )


    private val _cacheGivenMedicines = MutableStateFlow<List<InputFieldModel>>(emptyList())
    val cacheGivenMedicines: StateFlow<List<InputFieldModel>> = _cacheGivenMedicines
    fun getAttributeValueByCode(tei: TrackedEntityInstance?, code: String): String? {
        return tei?.trackedEntityAttributeValues()
            ?.find { it.trackedEntityAttribute() == code }
            ?.value()
    }
    fun getTeiData(uid: String) {
        Log.d("PRESC_VM","TEI_ID: $uid")
        viewModelScope.launch {
            val tei = repository.getPrescriptionPatient(uid, UIDMapping.PROGRAM_PU)

            val value = getAttributeValueByCode(tei, "KmR2FYgDUmr")
            Log.d("TEI_RES_IS:"," VALUE_IS $value")
            viewModelState.update {
                it.copy(
                    isLoading = false,
                    prescTei = tei
                )
            }
        }
    }

    fun loadPrescriptions(tei: String) {
        getTeiData(tei)
        viewModelScope.launch {
            val prescriptions = repository.getPrescriptions(
                tei = tei,
                program = UIDMapping.PROGRAM,
                stage = UIDMapping.PROGRAM_STAGE
            )

            viewModelState.update {
                it.copy(
                    isLoading = false,
                    prescriptions = prescriptions
                )
            }
        }
    }

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
                            dataElement = UIDMapping.DATA_ELEMENT_QTD_GIVEN,
                            value = prescriptionUiEvent.value,
                            conditionalValue = prescriptionUiEvent.prescription.requestedQtd.toString()
                        )
                    )
                } else {
                    cache.add(
                        InputFieldModel(
                            key = prescriptionUiEvent.prescription.uid,
                            dataElement = UIDMapping.DATA_ELEMENT_QTD_GIVEN,
                            value = prescriptionUiEvent.value,
                            conditionalValue = prescriptionUiEvent.prescription.requestedQtd.toString()
                        )
                    )
                }

                cache.removeIf { it.value.isEmpty() }

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
                viewModelScope.launch {
                    cacheGivenMedicines.value.map {
                        async {
                            repository.savePrescription(
                                event = it.key,
                                dataElement = it.dataElement,
                                value = it.value
                            )
                        }
                    }.awaitAll()

                    _cacheGivenMedicines.value = emptyList()
                    viewModelState.update { it.copy(isSaved = true) }
                }
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
        val completedCount = cacheGivenMedicines.value.count { it.value == it.conditionalValue }
        val incompleteCount = cacheGivenMedicines.value.count {
            it.value.toInt() < (it.conditionalValue?.toInt() ?: 0)
        }
        val zeroCount = viewModelState.value.prescriptions.size - cacheGivenMedicines.value.size

        return MedicineIndicators(
            Pair(R.string.completed_medicine, completedCount),
            Pair(R.string.incomplete_medicine, incompleteCount),
            Pair(R.string.non_existent_medicine, zeroCount)
        )
    }
}