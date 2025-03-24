package org.saudigitus.e_prescription.data.model

data class Prescription(
    val uid: String,
    val name: String,
    val requestedQtd: Int
)
