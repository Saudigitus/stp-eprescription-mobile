package org.saudigitus.e_prescription.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.saudigitus.e_prescription.presentation.theme.EprescriptionTheme

class EPrescriptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EprescriptionTheme {

            }
        }
    }
}
