package org.saudigitus.e_prescription.presentation.screens.login

import org.hisp.dhis.android.core.user.User

data class LoginResult(
    val success: User? = null,
    val error: String? = null
)