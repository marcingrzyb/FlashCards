package pl.edu.agh.kis.flashcards.api

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class RequestHelper {

    companion object {

        val POST_METHOD = "POST"

        @Throws(IOException::class)
        fun sendPost(url: String, request: String): String? {
            return send(url, request, POST_METHOD)
        }

        @Throws(IOException::class)
        private fun send(url: String, request: String, type: String): String? {
            val loginEndpoint = URL(url + request)
            val myConnection = loginEndpoint.openConnection() as HttpURLConnection
            myConnection.requestMethod = type
            myConnection.doOutput = true
            myConnection.doInput = true

            if (Objects.requireNonNull(myConnection).responseCode != 200) {
                throw RuntimeException("Connection Error")
            }
            val result = getResult(myConnection)
            myConnection.disconnect()
            return result
        }

        @Throws(IOException::class)
        private fun getResult(myConnection: HttpURLConnection): String {
            val br = BufferedReader(InputStreamReader(myConnection.inputStream))
            val sb = StringBuilder()
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            return sb.toString()
        }

    }

}