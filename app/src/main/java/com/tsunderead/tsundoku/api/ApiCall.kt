package com.tsunderead.tsundoku.api

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

@Suppress("DEPRECATION")
class ApiCall(val parent: NetworkCaller<JSONObject>, private val flag: Int = 0): android.os.AsyncTask<String, Void, JSONObject>() {

    private val tag = "API - call"
    private val apiUrl = "https://api.mangadex.org"

    @Deprecated("Deprecated in Java", ReplaceWith("super.onPreExecute()", "android.os.AsyncTask"))
    @Suppress("DEPRECATION")
    override fun onPreExecute() {
        super.onPreExecute()
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg param: String?): JSONObject {
        val url = URL("$apiUrl/${param[0]}")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"

        try {
            conn.connect()
        } catch (e: Exception) {
            e.printStackTrace()
            cancel(false)
        }

        val inputStream = InputStreamReader(conn.inputStream)
        val reader = BufferedReader(inputStream)

        val output = StringBuilder()

        var line: String?
        while (reader.readLine().also { line = it } != null) {
//            publishProgress()
            output.append(line)
        }
        return JSONObject(output.toString())
    }

    @Deprecated("Deprecated in Java")
    override fun onProgressUpdate(vararg values: Void?) {
        Log.i(tag, "New Line Received")
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: JSONObject?) {
        result!!.put("apiCallFlag", flag)
        parent.onCallSuccess(result)
    }

    @Deprecated("Deprecated in Java", ReplaceWith("parent.onCallFail()"))
    override fun onCancelled() {
        parent.onCallFail()
    }

}