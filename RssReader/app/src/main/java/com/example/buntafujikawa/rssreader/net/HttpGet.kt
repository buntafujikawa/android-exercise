package com.example.buntafujikawa.rssreader.net

import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * 指定したURLからダウンロードする
 */
class HttpGet(private var url: String) {

    private var status: Int? = null
    private lateinit var input: InputStream

    companion object {
        private const val CONNECT_TIMEOUT_MS: Int = 3000
        private const val READ_TIMEOUT_MS: Int = 5000
    }

    fun get(): Boolean {
        try {
            val url: URL = URL(this.url)

            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = CONNECT_TIMEOUT_MS
            connection.readTimeout = READ_TIMEOUT_MS
            connection.instanceFollowRedirects = true // リダイレクト許可

            connection.connect()

            status = connection.responseCode
            if (status!! >= 200 && status!! < 300) {
                input = BufferedInputStream(connection.inputStream)
                return true
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    fun getResponse(): InputStream {
        return input
    }

    fun getStatus(): Int {
        return status!!
    }
}
