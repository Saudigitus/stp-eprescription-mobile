package org.saudigitus.e_prescription.presentation.screens.prescriptions.model

data class InputFieldModel(
    val key: String,
    val value: String,
    val conditionalValue: String? = null,
) {
    fun hasError() = try {
        if (conditionalValue != null)
            value.toInt() > conditionalValue.toInt()
        else false
    } catch (_: Exception) {
        false
    }
}
