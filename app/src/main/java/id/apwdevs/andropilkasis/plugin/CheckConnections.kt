/**
 * Copyright @2019 by Alexzander Purwoko Widiantoro <purwoko908@gmail.com>
 * @author APWDevs
 *
 * Licensed under GNU GPLv3
 *
 * This module is provided by "AS IS" and if you want to take
 * a copy or modifying this module, you must include this @author
 * Thanks! Happy Coding!
 */

package id.apwdevs.andropilkasis.plugin

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class CheckConnections {
    fun isConnected(ctx: Context?, url: String, timeout: Int): Deferred<Boolean> = GlobalScope.async {
        if (ctx == null) return@async false

        val networkConnections = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = networkConnections.activeNetworkInfo
        if (!activeNetworkInfo.isConnected) return@async false
        var httpConnection: HttpURLConnection? = null
        try {
            httpConnection = URL(url).openConnection() as HttpURLConnection
            httpConnection.connectTimeout = timeout
            httpConnection.connect()
            //httpConnection.setRequestProperty("User-Agent", "test")
            //httpConnection.setRequestProperty("Connection", "close")
            httpConnection.responseCode == HttpURLConnection.HTTP_OK
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            httpConnection?.disconnect()
        }
    }
}