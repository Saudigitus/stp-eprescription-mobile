package org.saudigitus.e_prescription.data.model

data class MedicineIndicators(
    val completed: Pair<Int, Int>,
    val incomplete: Pair<Int, Int>,
    val zero: Pair<Int, Int>
)