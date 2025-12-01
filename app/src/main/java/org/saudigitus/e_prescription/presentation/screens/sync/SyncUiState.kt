package org.saudigitus.e_prescription.presentation.screens.sync

import org.saudigitus.e_prescription.R

enum class SyncStep {
    RUNNING,
    SUCCESS,
    FAILED
}

data class SyncUiState(
    val metadataLogo: Int = R.drawable.animator_sync,
    val metadataSyncStep: SyncStep? = null,
    val metadataSyncMsg: String? = null,
    val dataLogo: Int = R.drawable.animator_sync,
    val dataSyncStep: SyncStep? = null,
    val dataSyncMsg: String? = null,
    val errorLogo: Int? = null
)