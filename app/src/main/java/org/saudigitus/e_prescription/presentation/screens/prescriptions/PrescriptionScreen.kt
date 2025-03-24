package org.saudigitus.e_prescription.presentation.screens.prescriptions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.presentation.screens.prescriptions.components.ErrorBottomSheet
import org.saudigitus.e_prescription.presentation.screens.prescriptions.components.PrescriptionCard
import org.saudigitus.e_prescription.presentation.screens.prescriptions.components.SaveBottomSheet
import org.saudigitus.e_prescription.presentation.screens.prescriptions.model.InputFieldModel

val cardState = listOf(
    Prescription(
        uid = "iwue89qwh",
        name = "Paracetamol",
        requestedQtd = 1
    ),
    Prescription(
        uid = "290jqiw",
        name = "Dipirona",
        requestedQtd = 10
    ),
    Prescription(
        uid = "nfieo8239",
        name = "Ibuprofeno",
        requestedQtd = 10
    )
)

@Composable
fun PrescriptionScreen(
    modifier: Modifier = Modifier,
    viewModel: PrescriptionViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cache by viewModel.cacheGivenMedicines.collectAsStateWithLifecycle()

    if (uiState.displayErrors && uiState.errorState != null) {
        ErrorBottomSheet(uiState.errorState!!) {
            viewModel.onUiEvent(PrescriptionUiEvent.CloseErrorBottomSheet)
        }
    }

    if (uiState.isSaving && uiState.saveState != null) {
        SaveBottomSheet(
            state = uiState.saveState!!,
            onDismissRequest = { viewModel.onUiEvent(PrescriptionUiEvent.CloseSaveBottomSheet) }
        ) {
            viewModel.onUiEvent(PrescriptionUiEvent.OnSave)
        }
    }

    PrescriptionUI(modifier, uiState, cache) {
        when(it) {
            is PrescriptionUiEvent.OnBack -> {}
            else -> {
                viewModel.onUiEvent(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrescriptionUI(
    modifier: Modifier = Modifier,
    uiState: PrescriptionUiState,
    inputFieldModels: List<InputFieldModel>,
    onEvent: (PrescriptionUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.prescriptions),
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true,
                        maxLines = 1,
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onEvent(PrescriptionUiEvent.OnTempSave)
                },
            ) {
                Icon(Icons.Default.Save, contentDescription = stringResource(R.string.save))

                Text(stringResource(R.string.save))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!uiState.isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(uiState.prescriptions, key = { it.uid }) {
                        PrescriptionCard(
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 8.dp),
                            prescription = it,
                            inputFieldModels = inputFieldModels
                        ) { prescription, value ->
                            onEvent(
                                PrescriptionUiEvent.OnPrescriptionValueChange(
                                    prescription,
                                    value
                                )
                            )
                        }
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}