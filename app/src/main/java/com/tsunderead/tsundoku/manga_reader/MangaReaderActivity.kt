package com.tsunderead.tsundoku.manga_reader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.api.MangaChapter
import com.tsunderead.tsundoku.api.NetworkCaller
import com.tsunderead.tsundoku.chapter.chapter_page.ChapterPage
import com.tsunderead.tsundoku.chapter.chapter_page.ChapterPageAdapter
import com.tsunderead.tsundoku.databinding.MangaReaderBinding
import com.tsunderead.tsundoku.utils.OnSwipeTouchListener
import org.json.JSONObject
import kotlin.math.abs
import kotlin.properties.Delegates

class MangaReaderActivity : AppCompatActivity(), NetworkCaller<JSONObject> {
    private lateinit var mangaReaderBinding: MangaReaderBinding
    private var hidden: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mangaReaderBinding = MangaReaderBinding.inflate(layoutInflater)
        setContentView(mangaReaderBinding.root)
        val chapterId = intent.getStringExtra("ChapterId")
        chapterId?.let { MangaChapter(this, it) }?.execute()
        hideSystemBars()
    }

    @Suppress("DEPRECATION")
    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }


    override fun onCallSuccess(result: JSONObject?) {
        Log.i("MangaReaderActivity", result.toString())
        val chapterPages = ArrayList<ChapterPage>()
        for (key in result!!.keys()) {
            val chapterPage = ChapterPage(key.toInt(), result.getString(key))
            chapterPages.add(chapterPage)
        }
        val gestureDetector = GestureDetector(this@MangaReaderActivity, object: GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                Log.i("Very Weird", "This Behaviour")
                hidden = if (!hidden) {
                    mangaReaderBinding.nextChapter.hide()
                    mangaReaderBinding.previousChapter.hide()
                    mangaReaderBinding.goBack.hide()
                    true
                } else {
                    mangaReaderBinding.nextChapter.show()
                    mangaReaderBinding.previousChapter.show()
                    mangaReaderBinding.goBack.show()
                    false
                }
                return true
            }
        })
        val recyclerView = findViewById<RecyclerView>(R.id.mangaReaderRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return gestureDetector.onTouchEvent(e)
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                Log.i("In touch event", "?")
                super.onTouchEvent(rv, e)
            }
        })
        recyclerView.adapter = ChapterPageAdapter(chapterPages)
    }

}