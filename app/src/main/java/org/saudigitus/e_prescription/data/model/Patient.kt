package org.saudigitus.e_prescription.data.model

data class Patient(
    val uid: String,
    val name: Pair<String, String>,
    val surname: Pair<String, String>,
    val birthdate: Pair<String, String>,
    val residence: Pair<String, String>,
    val gender: Pair<String, String>,
    val processNumber: Pair<String, String>
)
