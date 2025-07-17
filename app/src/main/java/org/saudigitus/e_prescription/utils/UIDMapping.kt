package org.saudigitus.e_prescription.utils

object UIDMapping {
    const val PROGRAM = "raFdczPt1ky"
    const val PROGRAM_PU = "rTmLXWgtUj2"
    const val RELATIONSHIP_TYPE_UID = "I2vZoUvzeuu"
    const val OU_UID = "YsfZukmrl8z"
    const val PROGRAM_STAGE = "L2HtrpDbQ9y"
    const val DATA_ELEMENT_NAME = "hwSCSWIu3rp"
    const val DATA_ELEMENT_POSOLOGY = "nkCffVSBwS3"
    const val DATA_ELEMENT_QTD_REQ = "QFEmFThX9iE"
    const val DATA_ELEMENT_QTD_GIVEN = "qvRrBPpaIql"
    const val NAME_ATTR = "Jrd6W0L8LQY"
    const val SURNAME_ATTR = "KmR2FYgDUmr"
    const val BIRTHDATE_ATTR = "S5YtVz5P3QE"
    const val GENDER_ATTR = "CklPZdOd6H1"
    const val PROCESS_NUMBER_ATTR = "um3rU8yasxl"
    const val RESIDENCE_ATTR = "HKjREW796JR"


    fun attributes() = listOf(
        NAME_ATTR,
        SURNAME_ATTR,
        BIRTHDATE_ATTR,
        GENDER_ATTR,
        PROCESS_NUMBER_ATTR,
        RESIDENCE_ATTR
    )

    fun dataElements() = listOf(
        DATA_ELEMENT_NAME,
        DATA_ELEMENT_POSOLOGY,
        DATA_ELEMENT_QTD_REQ,
        DATA_ELEMENT_QTD_GIVEN
    )
}