package com.tsunderead.tsundoku.api

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class ApiCall(val parent: NetworkCaller<JSONObject>): AsyncTask<String, Void, JSONObject>() {

    private val tag = "API - call"
    private val apiUrl = "https://api.mangadex.org"

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg param: String?): JSONObject {

        val url = URL("$apiUrl/${param[0]}")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
//        Log.i(tag, conn.url.toString())
        conn.connect()

        val inputStream = InputStreamReader(conn.inputStream)
        val reader = BufferedReader(inputStream)

        val output = StringBuilder();

        var line: String?;
        while (reader.readLine().also { line = it } != null) {
            publishProgress()
            output.append(line)
        }
        return JSONObject(output.toString())
    }

    override fun onProgressUpdate(vararg values: Void?) {
        Log.i(tag, "New Line Received")
    }

    override fun onPostExecute(result: JSONObject?) {
        parent.onCallSuccess(result)
    }

    override fun onCancelled() {
        parent.onCallFail()
    }

}