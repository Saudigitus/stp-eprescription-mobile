package org.saudigitus.e_prescription.utils

import org.saudigitus.e_prescription.data.model.MedicineIndicators
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.data.model.PrescriptionError


fun Prescription.toPrescriptionError(givenQtd: Int) =
    PrescriptionError(
        uid = this.uid,
        name = this.name,
        requestedQtd = this.requestedQtd,
        givenQtd = givenQtd
    )

/**
 * returns indicators elements as list
 * Pair(string: label, int: count)
 */
fun MedicineIndicators.toList() =
    listOf(
        this.completed,
        this.incomplete,
        this.zero
    )