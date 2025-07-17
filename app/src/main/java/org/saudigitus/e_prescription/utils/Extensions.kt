package org.saudigitus.e_prescription.utils

import org.saudigitus.e_prescription.data.model.MedicineIndicators
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.data.model.PrescriptionError
import org.saudigitus.e_prescription.data.model.put.DataValue
import org.saudigitus.e_prescription.data.model.put.UpdateEvent
import org.saudigitus.e_prescription.data.model.response.Attribute


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

fun List<Attribute>.getByAttr(attr: String): Pair<String, String> {
    val attr = find { it.attribute == attr }

    return Pair(attr?.displayName.orEmpty(), attr?.value.orEmpty())
}

fun Prescription.toUpdateEvent(
    trackedEntityInstance: String,
    dataElement: String,
    value: Int
) =
    UpdateEvent(
        event = this.uid,
        orgUnit = this.ou,
        dataValues = listOf(
            DataValue(
                dataElement = dataElement,
                value = value
            )
        ),
        program = UIDMapping.PROGRAM,
        programStage = UIDMapping.PROGRAM_STAGE,
        status = "ACTIVE",
        trackedEntityInstance = trackedEntityInstance,
    )
