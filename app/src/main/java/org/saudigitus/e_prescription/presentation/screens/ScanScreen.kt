package org.saudigitus.e_prescription.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.utils.Utils

@Composable
fun ScanScreen(
    viewModel: ScanViewModel
) {
    ScanUI {
        when(it) {
            is ScanUiEvent.NavTo -> {

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
    onEvent: (ScanUiEvent) -> Unit,
) {
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
    ) { result ->
        onEvent(ScanUiEvent.Scan(result.contents))
    }

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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
        }
    }
}

