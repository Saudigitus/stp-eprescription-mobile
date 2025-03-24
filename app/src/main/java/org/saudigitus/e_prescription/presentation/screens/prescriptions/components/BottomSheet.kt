package org.saudigitus.e_prescription.presentation.screens.prescriptions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.presentation.screens.prescriptions.model.BottomSheetState
import org.saudigitus.e_prescription.presentation.theme.success
import org.saudigitus.e_prescription.presentation.theme.warning


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorBottomSheet(
    state: BottomSheetState.ErrorState,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = stringResource(R.string.failed_save, state.prescriptions.size),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = stringResource(R.string.failed_save, state.prescriptions.size),
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
            HorizontalDivider(thickness = 0.75.dp)
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(state.prescriptions, key = { it.uid }) {
                        PrescriptionCard(
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 5.dp),
                            prescription = it
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveBottomSheet(
    state: BottomSheetState.SaveState,
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .height(460.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = stringResource(R.string.summary_label),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = stringResource(R.string.summary_label),
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
            HorizontalDivider(thickness = 0.75.dp)
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        PrescriptionSummaryCard(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.Check,
                            title = stringResource(state.indicators.completed.first),
                            subtitle = "${state.indicators.completed.second}",
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.success,
                                contentColor = Color.White
                            )
                        )
                    }
                    item {
                        PrescriptionSummaryCard(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.Warning,
                            title = stringResource(state.indicators.incomplete.first),
                            subtitle = "${state.indicators.incomplete.second}",
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.warning,
                                contentColor = Color.White
                            )
                        )
                    }
                    item {
                        PrescriptionSummaryCard(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.Close,
                            title = stringResource(state.indicators.zero.first),
                            subtitle = "${state.indicators.zero.second}",
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = Color.White
                            )
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 0.75.dp)
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDismissRequest,
                ) {
                    Text(text = stringResource(R.string.cancel))
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onSave,
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}