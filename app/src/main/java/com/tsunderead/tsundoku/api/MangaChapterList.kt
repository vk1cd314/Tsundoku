package com.tsunderead.tsundoku.api

import android.util.Log
import org.json.JSONObject

class MangaChapterList(private val parent: NetworkCaller<JSONObject>, private val mangaID: String): NetworkCaller<JSONObject> {
    private val limit = 20

    fun execute(offset: Int) {
        val endpoint = "manga/$mangaID/feed/?offset=$offset&order%5Bchapter%5D=asc&translatedLanguage%5B%5D=en"
        Log.i("mangachapter", endpoint)
        ApiCall(this).execute(endpoint)
    }

    override fun onCallSuccess(result: JSONObject?) {
        val ret = JSONObject()

        try {
            val chapterList = result!!.getJSONArray("data")
            for (i in 0 until chapterList.length()) {
                val chapter = chapterList.getJSONObject(i)
                val jsonObj = JSONObject()
                jsonObj.put("chapterId", chapter.getString("id"))
                jsonObj.put("chapterNo", chapter.getJSONObject("attributes").getInt("chapter"))
                ret.put(i.toString(), jsonObj)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            parent.onCallSuccess(ret)
        }
    }

}