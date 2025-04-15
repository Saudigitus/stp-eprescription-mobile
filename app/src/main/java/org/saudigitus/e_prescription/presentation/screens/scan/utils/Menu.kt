package org.saudigitus.e_prescription.presentation.screens.scan.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Sync
import androidx.compose.ui.graphics.vector.ImageVector
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.presentation.screens.scan.ScanUiEvent

data class MenuItem(
    val icon: ImageVector,
    @StringRes val name: Int,
    val event: ScanUiEvent
)

object Menu {
    val items = listOf(
        MenuItem(
            icon = Icons.Default.Sync,
            name = R.string.sync_metadata,
            event = ScanUiEvent.SyncData
        ),
        MenuItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            name = R.string.logout,
            event = ScanUiEvent.Logout
        ),
    )
}