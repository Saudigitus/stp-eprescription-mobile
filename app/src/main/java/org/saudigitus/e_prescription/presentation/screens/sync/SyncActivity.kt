package org.saudigitus.e_prescription.presentation.screens.sync

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import org.saudigitus.e_prescription.presentation.EPrescriptionActivity
import org.saudigitus.e_prescription.presentation.theme.EPrescriptionTheme
import org.saudigitus.e_prescription.utils.Constants

@AndroidEntryPoint
class SyncActivity: ComponentActivity() {

    private val viewModel: SyncViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            viewModel.sync()

            EPrescriptionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SyncScreen(
                        viewModel,
                        navigateToHome = {
                            startActivity(Intent(this, EPrescriptionActivity::class.java))
                            finish()
                        }
                    )

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getSyncStatus(Constants.INITIAL_SYNC).observe(this) {
            it.forEach { workInfo ->
                if (workInfo.tags.contains(Constants.INSTANT_METADATA_SYNC)) {
                    viewModel.handleMetadataSync(workInfo)
                } else if (workInfo.tags.contains(Constants.INSTANT_DATA_SYNC)) {
                    viewModel.handleDataSync(workInfo)
                }
            }
        }
    }
}