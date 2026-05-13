package org.saudigitus.e_prescription.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtils(val context: Context) {
    fun isOnline(): Boolean {
        var isOnline = false
        try {
            val manager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val netInfo = manager.activeNetworkInfo
            isOnline = netInfo != null && netInfo.isConnectedOrConnecting
        } catch (_: Exception) {

        }
        return isOnline
    }


}