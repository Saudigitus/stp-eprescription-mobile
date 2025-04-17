package org.saudigitus.e_prescription.presentation.screens.login

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
import org.saudigitus.e_prescription.presentation.screens.sync.SyncActivity
import org.saudigitus.e_prescription.presentation.theme.EPrescriptionTheme

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EPrescriptionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    LoginScreen(
                        viewModel = viewModel ,
                        onNavigateToSync = {
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    SyncActivity::class.java
                                )
                            )
                            finish()
                        }
                    )

                }
            }
        }
    }
}
