package org.saudigitus.e_prescription.presentation.screens.sync

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.work.WorkInfo
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.utils.Constants

@Composable
fun Sync(
    syncViewModel: SyncViewModel,
    isSyncing: (Boolean) -> Unit = {},
    onSuccess: () -> Unit = {},
    onFail: () -> Unit = {}
) {
    var isSyncingRunning by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when(event) {
                Lifecycle.Event.ON_RESUME -> {
                    syncViewModel.getSyncStatus(Constants.ROUTINE_SYNC)
                        .observe(lifecycleOwner) {
                            it.forEach { workInfo ->
                                if (workInfo.tags.contains(Constants.INSTANT_DATA_SYNC)) {
                                    if (workInfo.state == WorkInfo.State.RUNNING) {
                                        isSyncingRunning = true
                                        isSyncing.invoke(true)
                                    } else if (workInfo.state == WorkInfo.State.SUCCEEDED && isSyncingRunning) {
                                        isSyncingRunning = false
                                        onSuccess.invoke()
                                        Toast.makeText(context, context.getString(R.string.success_data_sync), Toast.LENGTH_SHORT).show()
                                    } else if (workInfo.state == WorkInfo.State.FAILED && isSyncingRunning) {
                                        isSyncingRunning = false
                                        onFail.invoke()
                                        Toast.makeText(context, context.getString(R.string.error_data_sync), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}