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

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object GetServerData {
    @Throws(UnsupportedEncodingException::class)
    fun getPostDataString(params: HashMap<String, String>): String {
        val result = StringBuilder()
        var first = true

        for (entry in params.entries) {
            if (first)
                first = false
            else
                result.append("&")

            result.append(URLEncoder.encode(entry.key, "UTF-8"))
            result.append('=')
            result.append(URLEncoder.encode(entry.value, "UTF-8"))
        }
        return result.toString()
    }

    fun sendGetRequest(getUrl: String, timeout: Int): Deferred<String?> = GlobalScope.async {
        var httpURL: HttpURLConnection? = null
        try {
            httpURL = URL(getUrl).openConnection() as HttpURLConnection
            httpURL.doOutput = true
            httpURL.connectTimeout = timeout
            httpURL.readTimeout = timeout
            httpURL.connect()

            if (httpURL.responseCode == HttpURLConnection.HTTP_OK) {
                val sbuf = StringBuffer()
                val inputStream = httpURL.inputStream
                var read: Int
                while (true) {
                    read = inputStream.read()
                    if (read == -1) break
                    sbuf.append(read.toChar())
                }
                return@async sbuf.toString()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            httpURL?.disconnect()
        }
        null
    }

    fun sendPostRequest(requestURL: String, postDataParams: HashMap<String, String>, timeout: Int): Deferred<String> =
        GlobalScope.async {
            val url: URL

            val sb = StringBuilder()
            var conn: HttpURLConnection? = null
            try {
                url = URL(requestURL)
                conn = url.openConnection() as HttpURLConnection
                conn.readTimeout = timeout
                conn.connectTimeout = timeout
                conn.requestMethod = "POST"
                conn.doInput = true
                conn.doOutput = true
                val ostream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(ostream, "UTF-8"))
                writer.write(getPostDataString(postDataParams))
                writer.flush()
                writer.close()
                ostream.close()

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val iStreamReader = InputStreamReader(conn.inputStream)
                    val buffReader = BufferedReader(iStreamReader)
                    var response: String
                    while (true) {
                        response = buffReader.readLine() ?: break
                        sb.append(response)
                    }
                    buffReader.close()
                    iStreamReader.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                conn?.disconnect()
            }
            sb.toString()
        }
}