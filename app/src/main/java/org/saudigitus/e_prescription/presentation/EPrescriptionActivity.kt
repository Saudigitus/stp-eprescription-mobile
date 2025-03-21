package org.saudigitus.e_prescription.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import org.saudigitus.e_prescription.presentation.screens.ScanScreen
import org.saudigitus.e_prescription.presentation.screens.ScanViewModel
import org.saudigitus.e_prescription.presentation.theme.EPrescriptionTheme

@AndroidEntryPoint
class EPrescriptionActivity : ComponentActivity() {

    private val scanViewModel by viewModels<ScanViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EPrescriptionTheme {
                ScanScreen(scanViewModel)
            }
        }
    }
}
