package org.saudigitus.e_prescription.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.saudigitus.e_prescription.presentation.screens.prescriptions.PrescriptionScreen
import org.saudigitus.e_prescription.presentation.screens.prescriptions.PrescriptionViewModel
import org.saudigitus.e_prescription.presentation.screens.scan.ScanViewModel
import org.saudigitus.e_prescription.presentation.theme.EPrescriptionTheme

@AndroidEntryPoint
class EPrescriptionActivity : ComponentActivity() {

    private val scanViewModel by viewModels<ScanViewModel>()
    private val prescriptionViewModel by viewModels<PrescriptionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EPrescriptionTheme {
                PrescriptionScreen(viewModel = prescriptionViewModel)
            }
        }
    }
}
