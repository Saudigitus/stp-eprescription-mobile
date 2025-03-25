package org.saudigitus.e_prescription.presentation.screens.scan

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.journeyapps.barcodescanner.ScanContract
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.presentation.AppRoutes
import org.saudigitus.e_prescription.utils.Utils

@Composable
fun ScanScreen(
    viewModel: ScanViewModel,
    navTo: (route: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    ScanUI(state = uiState) {
        when(it) {
            is ScanUiEvent.NavTo -> {
                navTo(it.route)
            }
            else -> {
                viewModel.onUiEvent(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanUI(
    modifier: Modifier = Modifier,
    state: ScanUiState,
    onEvent: (ScanUiEvent) -> Unit,
) {
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
    ) { result ->
        onEvent(ScanUiEvent.Scan(result.contents))
    }

    var ref by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.scan_prescription),
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true,
                        maxLines = 1,
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.scan_label),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .padding(24.dp)
            )

            IconButton(
                modifier = Modifier.size(250.dp),
                onClick = {
                    scannerLauncher.launch(Utils.scanOptions())
                }
            ) {
                Icon(
                    modifier = Modifier.size(125.dp),
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = stringResource(R.string.scan_prescription)
                )
            }

            TextField(
                value = state.scanResult.ifEmpty { ref },
                onValueChange = { ref = it },

                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(24.dp)
            )

            Button(
                onClick = {
                    val uid = state.scanResult.ifEmpty { ref }
                    onEvent(ScanUiEvent.NavTo("${AppRoutes.PRESCRIPTION_SCREEN}/$uid"))
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
                    .height(108.dp)
                    .padding(24.dp)
            ) {
                Text(text = stringResource(R.string.validate))
                 Icon(
                     imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                     contentDescription = stringResource(R.string.validate)
                 )
            }
        }
    }
}

