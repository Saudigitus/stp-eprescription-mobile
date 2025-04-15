package org.saudigitus.e_prescription.utils

/**
 * @DHIS2
 */

object Constants {

    const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT = "yyyy-MM-dd"

    // SyncData parameters
    const val INITIAL_SYNC = "INITIAL_SYNC"
    const val ROUTINE_SYNC = "ROUTINE_SYNC"
    const val INSTANT_METADATA_SYNC = "INSTANT_METADATA_SYNC"
    const val INSTANT_DATA_SYNC = "INSTANT_DATA_SYNC"
    const val SCHEDULED_METADATA_SYNC = "SCHEDULED_METADATA_SYNC"
    const val SCHEDULED_DATA_SYNC = "SCHEDULED_DATA_SYNC"
    const val LAST_DATA_SYNC_DATE = "LAST_DATA_SYNC_DATE"
    const val LAST_DATA_SYNC_STATUS = "LAST_DATA_SYNC_STATUS"
    const val LAST_DATA_SYNC_RESULT = "LAST_DATA_SYNC_RESULT"
    const val LAST_METADATA_SYNC_DATE = "LAST_METADATA_SYNC_DATE"
    const val LAST_METADATA_SYNC_STATUS = "LAST_METADATA_SYNC_STATUS"
    const val SYNC_PERIOD_METADATA = "SYNC_PERIOD_METADATA"
    const val SYNC_PERIOD_DATA = "SYNC_PERIOD_DATA"
    const val SYNC_DATA_NOTIFICATION_CHANNEL = "SYNC_DATA_CHANNEL"
    const val SYNC_DATA_CHANNEL_NAME = "DATA_SYNC"
    const val SYNC_DATA_NOTIFICATION_ID = 710776
    const val SYNC_METADATA_NOTIFICATION_CHANNEL = "SYNC_METADATA_CHANNEL"
    const val SYNC_METADATA_CHANNEL_NAME = "METADATA_SYNC"
    const val SYNC_METADATA_NOTIFICATION_ID = 893455
    const val WORKER_ERROR_MESSAGE_KEY = "ERROR_MESSAGE"

    // Metadata & Data sync periods
    const val PERIOD_DAILY = 24 * 60 * 60
    const val PERIOD_WEEKLY = 7 * 24 * 60 * 60
    const val PERIOD_MANUAL = 0
    const val PERIOD_30M = 30 * 60
    const val PERIOD_1H = 60 * 60
    const val PERIOD_6H = 6 * 60 * 60
    const val PERIOD_12H = 12 * 60 * 60

    //Preferences
    const val SHARED_PREFS = "fisio_app"

    const val SERVER_URL = "SERVER_URL"
    const val USERNAME = "USERNAME"
    const val PASSWORD = "PASSWORD"

    const val DEFAULT = "default"

}