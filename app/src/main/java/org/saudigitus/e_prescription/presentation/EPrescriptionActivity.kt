package org.saudigitus.e_prescription.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import org.saudigitus.e_prescription.presentation.screens.prescriptions.PrescriptionScreen
import org.saudigitus.e_prescription.presentation.screens.prescriptions.PrescriptionViewModel
import org.saudigitus.e_prescription.presentation.screens.scan.ScanScreen
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
            EPrescriptionTheme (
                darkTheme = false
            ){
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.SCAN_SCREEN,
                ) {
                    composable(AppRoutes.SCAN_SCREEN){
                        ScanScreen(scanViewModel, navController::navigate)
                    }
                    composable(
                        route = "${AppRoutes.PRESCRIPTION_SCREEN}/{uid}",
                        arguments = listOf(navArgument("uid") { type = NavType.StringType })
                    ) { entry ->
                        val uid = entry.arguments?.getString("uid") ?: ""

                        PrescriptionScreen(prescriptionViewModel, uid, navController::navigateUp)
                    }
                }
            }
        }
    }
}
