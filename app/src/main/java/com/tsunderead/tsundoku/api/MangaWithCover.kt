package com.tsunderead.tsundoku.api

import android.util.Log
import com.tsunderead.tsundoku.ConstData
import org.json.JSONArray
import org.json.JSONObject

class MangaWithCover(
    private val parent: NetworkCaller<JSONObject>,
    private val filter: HashMap<String, ArrayList<String>> = HashMap(),
) : NetworkCaller<JSONObject> {

    private val limit = 10
    private val tag = "MangaWithCover"
    private lateinit var mangaList: JSONArray
    private lateinit var returnObj: JSONObject
    private var populatedWithCover = false
    private var populatedWithAuthor = false

    fun execute(offset: Int) {
        val endpoint = "manga?limit=$limit&offset=$offset${generateFilter()}"
        Log.i(tag, endpoint)
        @Suppress("DEPRECATION") ApiCall(this).execute(endpoint)
    }

    private fun generateFilter(): String {
        ConstData().tagMap
        if (filter.isEmpty()) return ""
        var str = ""
        for (key in filter.keys) {
            val filterKey = filter[key]
            if (key == "includedTags%5B%5D") {
                val tagMap = ConstData().tagMap
                for (filterVal in filterKey!!) str = "$str&$key=${tagMap[filterVal]}"
            } else for (filterVal in filterKey!!) str = "$str&$key=$filterVal"
        }
        return str
    }

    override fun onCallSuccess(result: JSONObject?) {
        try {
            if (!result!!.getString("result").equals(("ok"))) throw Exception("Not OK")
            mangaList = result.getJSONArray("data")
            returnObj = JSONObject()
            for (i in 0 until mangaList.length()) {
                val manga = mangaList.getJSONObject(i)

//                Log.e(tag, manga.toString())

                val jsonObject = JSONObject()
                jsonObject.put("id", manga.getString("id"))


                try {
                    jsonObject.put(
                        "name",
                        manga.getJSONObject("attributes").getJSONObject("title").getString("en")
                    )
                } catch (e: Exception) {
                    for (nameLang in manga.getJSONObject("attributes").getJSONObject("title")
                        .keys()) {
                        jsonObject.put(
                            "name",
                            manga.getJSONObject("attributes").getJSONObject("title")
                                .getString(nameLang)
                        )
                        break
                    }
                }

                returnObj.put(i.toString(), jsonObject)
            }

            MangaCover(this).execute()
            MangaAuthor(this).execute()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun populateWithCover(mangaCoverMap: LinkedHashMap<String, String>) {

        for (i in 0 until returnObj.length()) {
            val manga = returnObj.getJSONObject(i.toString())
            try {
                manga.put("cover_art", mangaCoverMap[manga.getString("id")])
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        populatedWithCover = true
        onPopulateComplete()
    }

    fun populateWithAuthor(mangaAuthorMap: LinkedHashMap<String, String>) {
        for (i in 0 until returnObj.length()) {
            val manga = returnObj.getJSONObject(i.toString())
            try {
                manga.put("author", mangaAuthorMap[manga.getString("id")])
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        populatedWithAuthor = true
        onPopulateComplete()
    }

    private fun onPopulateComplete() {

        if (populatedWithCover && populatedWithAuthor) parent.onCallSuccess(returnObj)

    }

    class MangaCover(private val mangaWithoutCover: MangaWithCover) : NetworkCaller<JSONObject> {

        private val totalManga = mangaWithoutCover.mangaList.length()
        private val mangaCoverMap = LinkedHashMap<String, String>()
        private var mangaCounter = 0

        fun execute() {
            for (i in 0 until mangaWithoutCover.mangaList.length()) {
                try {

                    val manga = mangaWithoutCover.mangaList.getJSONObject(i)
                    val relationships = manga.getJSONArray("relationships")

                    for (rel in 0 until relationships.length()) if (relationships.getJSONObject(rel)
                            .getString("type").equals("cover_art")
                    ) {

                        @Suppress("DEPRECATION") ApiCall(this, i).execute(
                            "cover/${
                                relationships.getJSONObject(rel).getString("id")
                            }"
                        )
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

                val pos = result.getInt("apiCallFlag")
                mangaCoverMap[mangaWithoutCover.mangaList.getJSONObject(pos).getString("id")] =
                    "https://uploads.mangadex.org/covers/${
                        mangaWithoutCover.mangaList.getJSONObject(pos).getString("id")
                    }/${
                        result.getJSONObject("data").getJSONObject("attributes")
                            .getString("fileName")
                    }"

                if (++mangaCounter == totalManga) mangaWithoutCover.populateWithCover(mangaCoverMap)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    class MangaAuthor(private val mangaWithoutAuthor: MangaWithCover) : NetworkCaller<JSONObject> {

        private val totalManga = mangaWithoutAuthor.mangaList.length()
        private val mangaAuthorMap = LinkedHashMap<String, String>()
        private var mangaCounter = 0

        fun execute() {
            for (i in 0 until mangaWithoutAuthor.mangaList.length()) {
                try {
                    val manga = mangaWithoutAuthor.mangaList.getJSONObject(i)
                    val relationships = manga.getJSONArray("relationships")

                    for (rel in 0 until relationships.length()) if (relationships.getJSONObject(rel)
                            .getString("type").equals("author")
                    ) {

                        @Suppress("DEPRECATION") ApiCall(this, i).execute(
                            "author/${
                                relationships.getJSONObject(rel).getString("id")
                            }"
                        )
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
                val pos = result.getInt("apiCallFlag")
                mangaAuthorMap[mangaWithoutAuthor.mangaList.getJSONObject(pos).getString("id")] =
                    result.getJSONObject("data").getJSONObject("attributes").getString("name")

                if (++mangaCounter == totalManga) mangaWithoutAuthor.populateWithAuthor(
                    mangaAuthorMap
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}