package com.tsunderead.tsundoku.api

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class MangaWithCover(private val parent: NetworkCaller<JSONObject>): NetworkCaller<JSONObject> {

    private val limit = 10
    val tag = "MangaWithCover"
    private lateinit var mangaList: JSONArray

    fun execute(offset: Int) {
        val endpoint = "manga?limit=$limit&offset=$offset"
        ApiCall(this).execute(endpoint)
    }

    override fun onCallSuccess(result: JSONObject?) {
        try {
            if (!result!!.getString("result").equals(("ok"))) throw Exception("Not OK")
            mangaList = result.getJSONArray("data")
            MangaCover(this).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onPopulateComplete(mangaCoverMap: LinkedHashMap<String, String>) {
        val ret = JSONObject()

        for (i in 0 until mangaList.length()) {
            val mangaObj = mangaList.getJSONObject(i)
            val jsonObj = JSONObject()
            try {
                jsonObj.put("id", mangaObj.getString("id"))
                jsonObj.put("name", mangaObj.getJSONObject("attributes").getJSONObject("title").getString("en"));
                jsonObj.put("cover_art", mangaCoverMap[mangaObj.getString("id")])
            } catch (e: Exception) {
                e.printStackTrace()
            }
            ret.put("$i", jsonObj)
        }
        parent.onCallSuccess(ret)
    }

    class MangaCover(private val mangaWithoutCover: MangaWithCover): NetworkCaller<JSONObject> {

        private val totalManga = mangaWithoutCover.mangaList.length()
        private val mangaCoverMap = LinkedHashMap<String, String>()
        private var mangaCounter = 0;

        fun execute() {
            for (i in 0 until mangaWithoutCover.mangaList.length()) {
                try {

                    val manga = mangaWithoutCover.mangaList.getJSONObject(i)
                    val relationships = manga.getJSONArray("relationships")

                    for (rel in 0 until relationships.length())
                        if (relationships.getJSONObject(rel).getString("type").equals("cover_art")){
                            ApiCall(this).execute("cover/${relationships.getJSONObject(rel).getString("id")}")
                            break
                        }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onCallSuccess(result: JSONObject?) {
            try {
                if (!result!!.getString("result").equals(("ok"))) throw Exception("Not OK")

                val relationship = result.getJSONObject("data").getJSONArray("relationships")
                for (rel in 0 until relationship.length())
                    if (relationship.getJSONObject(rel).getString("type").equals("manga")) {
                        mangaCoverMap[relationship.getJSONObject(rel).getString("id")] = "https://uploads.mangadex.org/covers/${relationship.getJSONObject(rel).getString("id")}/${result.getJSONObject("data").getJSONObject("attributes").getString("fileName")}"
                        break
                    }

                if (++mangaCounter == totalManga) mangaWithoutCover.onPopulateComplete(mangaCoverMap)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

}