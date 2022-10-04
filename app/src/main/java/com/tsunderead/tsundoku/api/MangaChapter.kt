package com.tsunderead.tsundoku.api

import org.json.JSONObject

class MangaChapter(val parent: NetworkCaller<JSONObject>, private val chapterId: String): NetworkCaller<JSONObject> {

    fun execute() {
        val endpoint = "at-home/server/$chapterId"

        @Suppress("DEPRECATION")
        ApiCall(this).execute(endpoint)
    }

    override fun onCallSuccess(result: JSONObject?) {
        try {
            if (!result!!.getString("result").equals("ok")) throw Exception("Not Ok")

            val ret = JSONObject()
            val urlPref = "${result.getString("baseUrl")}/data/${result.getJSONObject("chapter").getString("hash")}"
            val pageList = result.getJSONObject("chapter").getJSONArray("data")
            for (i in 0 until pageList.length())
                ret.put(i.toString(), "$urlPref/${pageList.getString(i)}")
//            Log.i("ok", ret.toString())
            parent.onCallSuccess(ret)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}