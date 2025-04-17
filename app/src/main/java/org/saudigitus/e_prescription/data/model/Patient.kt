package org.saudigitus.e_prescription.data.model

data class Patient (
    val uid: String,
    val name: String,
    val surname: String,
    val residence: String,
    val gender: String,
    val processNumber: String
)
