package org.saudigitus.e_prescription.utils

import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance

class AttributesHalper {
    fun getAttributeValueByCode(tei: TrackedEntityInstance?, code: String): String? {
        return tei?.trackedEntityAttributeValues()
            ?.find { it.trackedEntityAttribute() == code }
            ?.value()
    }
}