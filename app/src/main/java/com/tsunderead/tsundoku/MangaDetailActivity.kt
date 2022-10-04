package com.tsunderead.tsundoku

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.api.MangaChapterList
import com.tsunderead.tsundoku.api.NetworkCaller
import org.json.JSONObject

class MangaDetailActivity : AppCompatActivity(), NetworkCaller<JSONObject>{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_detail)
        val cover = intent.getStringExtra("Cover")
        val author = intent.getStringExtra("Author")
        val title = intent.getStringExtra("Title")
        println("Cover is $cover")
        println("Title is $title")
        println("Author is $author")
        val authorId = findViewById<TextView>(R.id.author)
        val titleId = findViewById<TextView>(R.id.title)
        val coverId = findViewById<ImageView>(R.id.mangacover)
        authorId.text = author
        titleId.text = title
//        coverId.setImageResource(cover)
        val mangaId = intent.getStringExtra("MangaID")
        ImageFromInternet(coverId).execute(cover)
        mangaId?.let { MangaChapterList(this, it) }?.execute(0)
    }
    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class ImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
//            Toast.makeText(this, "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show()
            Log.e("Hello", "Working")
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        @Deprecated("Deprecated in Java", ReplaceWith("imageView.setImageBitmap(result)"))
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }

    override fun onCallSuccess(result: JSONObject?) {
//        TODO("Not yet implemented")
        Log.i("ok", result.toString())
        val recyclerView = findViewById<RecyclerView>(R.id.chapterRecyclerView)
        val layoutManager = LinearLayoutManager(this@MangaDetailActivity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val chapters = ArrayList<Chapter>()
        for (key in result!!.keys()) {
            val chapter = Chapter(result.getJSONObject(key).getInt("chapterNo"), result.getJSONObject(key).getString("chapterId"))
            chapters.add(chapter)
        }
        recyclerView.adapter = ChapterAdapter(chapters)
    }
}