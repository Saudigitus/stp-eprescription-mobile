package org.saudigitus.e_prescription.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * The success color is used to indicate success in components, such as valid text in a text field.
 */
val ColorScheme.success: Color
    @Composable
    get() = if (isSystemInDarkTheme()) darkSuccess else lightSuccess

/**
 * The warning color is used to indicate warnings in components, such as potential issues that might cause problems later.
 */
val ColorScheme.warning: Color
    @Composable
    get() = if (isSystemInDarkTheme()) darkWarning else lightWarning

