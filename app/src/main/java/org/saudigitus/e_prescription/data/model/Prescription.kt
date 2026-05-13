package org.saudigitus.e_prescription.data.model

data class Prescription(
    val uid: String,
    val ou: String,
    val name: String,
    val posology: String,
    val requestedQtd: Int,
    val completedQtd: Int = 0,
    val isCompleted: Boolean = false
)
