package org.saudigitus.e_prescription.utils

import android.content.Context
import android.net.Uri
import org.hisp.dhis.android.core.settings.DataSyncPeriod
import org.hisp.dhis.android.core.settings.MetadataSyncPeriod
import java.io.File
import java.io.FileOutputStream
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

const val EVERY_30_MIN = 30 * 60
const val EVERY_HOUR = 60 * 60
const val EVERY_6_HOUR = 6 * 60 * 60
const val EVERY_12_HOUR = 12 * 60 * 60
const val EVERY_24_HOUR = 24 * 60 * 60
const val EVERY_7_DAYS = 7 * 24 * 60 * 60
const val MANUAL = 0

fun MetadataSyncPeriod.toSeconds(): Int {
    return when (this) {
        MetadataSyncPeriod.EVERY_HOUR -> EVERY_HOUR
        MetadataSyncPeriod.EVERY_12_HOURS -> EVERY_12_HOUR
        MetadataSyncPeriod.EVERY_24_HOURS -> EVERY_24_HOUR
        MetadataSyncPeriod.EVERY_7_DAYS -> EVERY_7_DAYS
        MetadataSyncPeriod.MANUAL -> MANUAL
    }
}

fun DataSyncPeriod.toSeconds(): Int {
    return when (this) {
        DataSyncPeriod.EVERY_30_MIN -> EVERY_30_MIN
        DataSyncPeriod.EVERY_HOUR -> EVERY_HOUR
        DataSyncPeriod.EVERY_6_HOURS -> EVERY_6_HOUR
        DataSyncPeriod.EVERY_12_HOURS -> EVERY_12_HOUR
        DataSyncPeriod.EVERY_24_HOURS -> EVERY_24_HOUR
        DataSyncPeriod.MANUAL -> MANUAL
    }
}

fun Uri.toFile(context: Context): File? {
    val inputStream = context.contentResolver.openInputStream(this)
    inputStream?.use { input ->
        val file = File.createTempFile("temp", null, context.cacheDir)
        FileOutputStream(file).use { output ->
            input.copyTo(output)
            return file
        }
    }
    return null
}


fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.getDisplayName(TextStyle.FULL, Locale("PT"))} ${this.year}"
}