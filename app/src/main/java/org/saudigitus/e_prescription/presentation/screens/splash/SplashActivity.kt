package org.saudigitus.e_prescription.presentation.screens.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.presentation.EPrescriptionActivity
import org.saudigitus.e_prescription.presentation.screens.login.LoginActivity
import org.saudigitus.e_prescription.presentation.theme.EPrescriptionTheme

@SuppressLint(value = ["CustomSplashScreen"])
@AndroidEntryPoint
class SplashActivity: ComponentActivity() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            EPrescriptionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    var hasPermissions by remember {
                        mutableStateOf(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                ContextCompat.checkSelfPermission(
                                    this, Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(
                                        this, Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                            } else {
                                ContextCompat.checkSelfPermission(
                                    this, Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            }
                        )
                    }

                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions(),
                        onResult = { permissions ->
                            hasPermissions = permissions.values.all { it }
                        }
                    )

                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(
                        lifecycleOwner,
                        effect = {
                            val observer = LifecycleEventObserver { _, event ->
                                if (event == Lifecycle.Event.ON_START) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.POST_NOTIFICATIONS,
                                            )
                                        )
                                    } else {
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.CAMERA,
                                            )
                                        )
                                    }
                                }
                            }
                            lifecycleOwner.lifecycle.addObserver(observer)

                            onDispose {
                                lifecycleOwner.lifecycle.removeObserver(observer)
                            }
                        }
                    )

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier)
                        Image(
                            painter = painterResource(R.drawable.stp_logo),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .size(200.dp),
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            CircularProgressIndicator(
                                color = Color.White
                            )
                        }

                    }

                    viewModel.isLoggedIn.collectAsStateWithLifecycle().value?.let {
                        if (hasPermissions) {
                            if (!it) {
                                startActivity(
                                    Intent(
                                        this@SplashActivity,
                                        LoginActivity::class.java
                                    )
                                )
                            } else {
                                startActivity(
                                    Intent(
                                        this@SplashActivity,
                                        EPrescriptionActivity::class.java
                                    )
                                )
                            }
                            finish()
                        }
                    }

                }
            }
        }
    }
}