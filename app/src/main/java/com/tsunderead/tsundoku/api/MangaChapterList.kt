package com.tsunderead.tsundoku.api

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class MangaChapterList(private val parent: NetworkCaller<JSONObject>, private val mangaID: String) :
    NetworkCaller<JSONObject> {

    private val returnObj = JSONObject()
    private var populatedWithChapterList = false
    private var populatedWithDescription = false

    fun execute(offset: Int) {
        val endpoint =
            "manga/$mangaID/feed/?offset=$offset&order%5Bchapter%5D=asc&translatedLanguage%5B%5D=en"
//        Log.i("mangachapter", endpoint)
        ApiCall(this).execute(endpoint)
        MangaDetails(this).execute()
    }

    override fun onCallSuccess(result: JSONObject?) {
        if ((result == null) || (result.getString("result") != "ok")) {
            onCallFail()
            return
        } else populateWithChapterList(result)
    }

    private fun populateWithChapterList(result: JSONObject) {
        try {
            val chapterList = result.getJSONArray("data")
            for (i in 0 until chapterList.length()) {
                val chapter = chapterList.getJSONObject(i)
                val jsonObj = JSONObject()
                jsonObj.put("chapterId", chapter.getString("id"))
                jsonObj.put("chapterNo", chapter.getJSONObject("attributes").getInt("chapter"))
                returnObj.put(i.toString(), jsonObj)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            populatedWithChapterList = true
            if (populatedWithChapterList && populatedWithDescription) {
                parent.onCallSuccess(returnObj)
            }
        }

    }

    fun populateWithDetails(result: JSONObject) {
        val manga = result.getJSONObject("data")
        try {
            returnObj.put(
                "description",
                manga.getJSONObject("attributes").getJSONObject("description").getString("en")
            )
        } catch (e: Exception) {
            for (descriptionLang in manga.getJSONObject("attributes").getJSONObject("description")
                .keys()) {
                returnObj.put(
                    "description",
                    manga.getJSONObject("attributes").getJSONObject("description")
                        .getString(descriptionLang)
                )
                break
            }
        }

        val tags = JSONArray()
        val tagList = manga.getJSONObject("attributes").getJSONArray("tags")
        for (j in 0 until tagList.length()) {

            try {
                tags.put(
                    j,
                    tagList.getJSONObject(j).getJSONObject("attributes").getJSONObject("name")
                        .getString("en")
                )
            } catch (e: Exception) {
                for (tag in tagList.getJSONObject(j).getJSONObject("attributes")
                    .getJSONObject("name").keys()) {
                    tags.put(
                        j,
                        tagList.getJSONObject(j).getJSONObject("attributes").getJSONObject("name")
                            .getString(tag)
                    )
                    break
                }
            }
        }
        returnObj.put("tags", tags)

        populatedWithDescription = true
        if (populatedWithChapterList && populatedWithDescription) {
            Log.i("inside", returnObj.toString())
            parent.onCallSuccess(returnObj)
        }
    }

    private class MangaDetails(private val manga: MangaChapterList) : NetworkCaller<JSONObject> {

        fun execute() {
            val endpoint = "manga/${manga.mangaID}"
            ApiCall(this).execute(endpoint)
        }

        override fun onCallSuccess(result: JSONObject?) {
            if ((result == null) || (result.getString("result") != "ok")) {
                onCallFail()
                return
            } else manga.populateWithDetails(result)
        }

    }

}