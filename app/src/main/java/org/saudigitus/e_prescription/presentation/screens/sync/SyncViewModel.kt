package org.saudigitus.e_prescription.presentation.screens.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.data.remote.SyncManagerRepository
import org.saudigitus.e_prescription.utils.ResourceManager
import javax.inject.Inject

@HiltViewModel
class SyncViewModel
@Inject constructor(
    private val syncManager: SyncManagerRepository,
    private val resourceManager: ResourceManager
): ViewModel() {

    private val _syncUiState = MutableStateFlow(
        SyncUiState(
            metadataLogo = R.drawable.animator_sync,
            dataLogo = R.drawable.animator_sync,
            metadataSyncMsg = resourceManager.getString(R.string.syncing_configuration),
            dataSyncMsg = resourceManager.getString(R.string.syncing_data_shortly)
        )
    )

    val syncUiState = _syncUiState.asStateFlow()

    fun sync() {
        syncManager.sync()
    }

    fun syncData() {
        syncManager.syncDataWithTrigger()
    }

    fun getSyncStatus(workName: String) = syncManager.getSyncStatus(workName)

    fun handleMetadataSync(workInfo: WorkInfo) {
        when (workInfo.state) {
            WorkInfo.State.RUNNING -> {
                _syncUiState.update {
                    it.copy(metadataSyncStep = SyncStep.RUNNING, metadataLogo = R.drawable.animator_sync)
                }
            }
            WorkInfo.State.SUCCEEDED -> {
                _syncUiState.update {
                    it.copy(
                        metadataLogo = R.drawable.animator_done,
                        metadataSyncStep = SyncStep.SUCCESS,
                        metadataSyncMsg = resourceManager.getString(R.string.configuration_ready)
                    )
                }
            }
            WorkInfo.State.FAILED -> {
                _syncUiState.update {
                    it.copy(
                        errorLogo = R.drawable.ic_sync_problem_red,
                        metadataSyncStep = SyncStep.FAILED,
                        metadataSyncMsg = resourceManager.getString(R.string.configuration_sync_failed)
                    )
                }
            }
            else -> {}
        }
    }

    fun handleDataSync(workInfo: WorkInfo) {
        when (workInfo.state) {
            WorkInfo.State.RUNNING -> {
                _syncUiState.update {
                    it.copy(
                        dataLogo = R.drawable.animator_sync,
                        dataSyncStep = SyncStep.RUNNING,
                        dataSyncMsg = resourceManager.getString(R.string.syncing_data)
                    )
                }
            }
            WorkInfo.State.SUCCEEDED -> {
                _syncUiState.update {
                    it.copy(
                        dataLogo = R.drawable.animator_done,
                        dataSyncStep = SyncStep.SUCCESS,
                        dataSyncMsg = resourceManager.getString(R.string.data_ready)
                    )
                }
                viewModelScope.launch { delay(1000) }
            }
            WorkInfo.State.FAILED -> {
                _syncUiState.update {
                    it.copy(
                        errorLogo = R.drawable.ic_sync_problem_red,
                        dataSyncStep = SyncStep.FAILED,
                        dataSyncMsg = resourceManager.getString(R.string.data_sync_failed)
                    )
                }
            }
            else -> {}
        }
    }
}