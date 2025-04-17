package org.saudigitus.e_prescription.utils

import org.saudigitus.e_prescription.R


/**
 * QUADRAM. Created by ppajuelo on 16/01/2018.
 */
enum class Period(
    val id: Int
) {
    NONE(R.string.period),
    DAILY(R.string.DAILY),
    WEEKLY(R.string.WEEKLY),
    MONTHLY(R.string.MONTHLY),
    YEARLY(R.string.YEARLY);

    open fun getNameResouce(): Int {
        return id
    }

}