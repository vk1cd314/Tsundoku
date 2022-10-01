package com.tsunderead.tsundoku.api

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class ApiCall: AsyncTask<String, Void, String>() {

    val tag = "API - call"
    val api_url = "https://api.mangadex.org"
    val output = StringBuilder();

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg param: String?): String {

        val url = URL("$api_url/${param[0]}")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connect()

        val inputStream = InputStreamReader(conn.inputStream)
        val reader = BufferedReader(inputStream)

        output.clear()

        var line: String?;
        while(reader.readLine().also { line = it} != null) {
            publishProgress()
            output.append(line)
        }
        Log.i(tag, output.toString())
        return output.toString();
    }

    override fun onProgressUpdate(vararg values: Void?) {
        Log.i(tag, "New Line Received")
    }`

    override fun onPostExecute(result: String?) {
        Log.i(tag, result!!)
    }

}