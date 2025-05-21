package com.tsunderead.tsundoku.manga_reader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.api.MangaChapter
import com.tsunderead.tsundoku.api.NetworkCaller
import com.tsunderead.tsundoku.chapter.chapter_page.ChapterPage
import com.tsunderead.tsundoku.chapter.chapter_page.ChapterPageAdapter
import com.tsunderead.tsundoku.databinding.MangaReaderBinding
import com.tsunderead.tsundoku.offlinedb.LibraryDBHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.properties.Delegates

class MangaReaderActivity : AppCompatActivity(), NetworkCaller<JSONObject> {
    private lateinit var mangaReaderBinding: MangaReaderBinding
    private lateinit var chapterList: ArrayList<String>
    private var position by Delegates.notNull<Int>()
    private lateinit var libraryDBHandler: LibraryDBHelper
    private var mangaId: String? = null
    private var chapterNumlist: ArrayList<String>? = null
    private lateinit var recyclerView: RecyclerView // Added for easier access

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mangaReaderBinding = MangaReaderBinding.inflate(layoutInflater)
        setContentView(mangaReaderBinding.root)
        chapterNumlist = intent.getStringArrayListExtra("chapterNumlist")
        val chapterNum = intent.getStringExtra("ChapterNum")
        mangaId = intent.getStringExtra("MangaId")
        val chapterId = intent.getStringExtra("ChapterId")
        chapterList = intent.getStringArrayListExtra("chapterList")!!
        position = intent.getIntExtra("position", -1)
        // Log.i("Stuff1", chapterId.toString())
        // Log.i("Stuff2", chapterList.toString())
        // Log.i("Stuff3", position.toString())
        libraryDBHandler = LibraryDBHelper(this@MangaReaderActivity, null)
        recyclerView = mangaReaderBinding.mangaReaderRecyclerView // Initialize RecyclerView
        recyclerView.setHasFixedSize(true)

        // Log.i("Before here", "Def")
        if (mangaId?.let { libraryDBHandler.isPresent(it) } == true) chapterId?.let {
            if (chapterNum != null) {
                libraryDBHandler.updateHistoryChapterRead(mangaId!!, // mangaId is now a class member
                    it, chapterNum)
                // Log.i("In here", "Def")
            }
        }
        chapterId?.let { MangaChapter(this, it) }?.execute()
        hideSystemBars()
        mangaReaderBinding.goBack.setOnClickListener {
            mangaReaderBinding.goBack.hide()
            finish()
        }
        mangaReaderBinding.nextChapter.setOnClickListener {
            mangaReaderBinding.nextChapter.hide()
            mangaReaderBinding.previousChapter.hide()
            mangaReaderBinding.goBack.hide()
            if (position == chapterList.size - 1) {
                Toast.makeText(this@MangaReaderActivity, "No Chapter After", Toast.LENGTH_SHORT)
                    .show()
            } else {
                position++
                val newChapterId = chapterList[position]
                val newChapterNum = chapterNumlist?.get(position)
                if (newChapterId != null && newChapterNum != null && mangaId != null) {
                    MangaChapter(this@MangaReaderActivity, newChapterId).execute()
                    libraryDBHandler.updateHistoryChapterRead(mangaId!!, newChapterId, newChapterNum)
                    // Clear RecyclerView
                    recyclerView.adapter = null
                } else {
                    Toast.makeText(this@MangaReaderActivity, "Error loading chapter data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        mangaReaderBinding.previousChapter.setOnClickListener {
            mangaReaderBinding.previousChapter.hide()
            mangaReaderBinding.nextChapter.hide()
            mangaReaderBinding.goBack.hide()
            if (position == 0) {
                Toast.makeText(this@MangaReaderActivity, "No Chapter Before", Toast.LENGTH_SHORT)
                    .show()
            } else {
                position--
                val newChapterId = chapterList[position]
                val newChapterNum = chapterNumlist?.get(position)
                if (newChapterId != null && newChapterNum != null && mangaId != null) {
                    MangaChapter(this@MangaReaderActivity, newChapterId).execute()
                    libraryDBHandler.updateHistoryChapterRead(mangaId!!, newChapterId, newChapterNum)
                    // Clear RecyclerView
                    recyclerView.adapter = null
                } else {
                    Toast.makeText(this@MangaReaderActivity, "Error loading chapter data", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
        val chapterPages = ArrayList<ChapterPage>() // Cleared by re-initialization
        if (result != null) {
            for (key in result.keys()) {
                try {
                    val pageNumber = key.toInt()
                    val chapterPage = ChapterPage(pageNumber, result.getString(key))
                    chapterPages.add(chapterPage)
                } catch (e: NumberFormatException) {
                    Log.e("MangaReaderActivity", "Invalid page number format for key: $key", e)
                    continue // Skip this entry and continue with the next key
                }
            }
        } else {
            // Handle null result, maybe show an error message
            Toast.makeText(this@MangaReaderActivity, "Failed to load chapter pages.", Toast.LENGTH_LONG).show()
            // Potentially load a placeholder or allow retry
            return // Exit if there's no data to display
        }

        // It's good practice to sort pages by key if not already sorted
        chapterPages.sortBy { it.pageNo }

        val gestureDetector = GestureDetector(
            this@MangaReaderActivity,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    // Log.i("Very Weird", "This Behaviour")
                    // Toggle visibility of controls
                    val controlsVisible = mangaReaderBinding.nextChapter.isVisible
                    mangaReaderBinding.nextChapter.visibility = if (controlsVisible) android.view.View.GONE else android.view.View.VISIBLE
                    mangaReaderBinding.previousChapter.visibility = if (controlsVisible) android.view.View.GONE else android.view.View.VISIBLE
                    mangaReaderBinding.goBack.visibility = if (controlsVisible) android.view.View.GONE else android.view.View.VISIBLE
                    return true
                }
            })
        // recyclerView is already initialized in onCreate and setHasFixedSize is called there.
        // We only need to set up the adapter and touch listener if it's the first time or they need reconfiguration.
        // For this refactor, simply setting a new adapter is fine.
        recyclerView.clearOnItemTouchListeners() // Clear previous listeners if any
        recyclerView.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return gestureDetector.onTouchEvent(e)
            }
        })
        recyclerView.adapter = ChapterPageAdapter(chapterPages)
        // Potentially scroll to top
        recyclerView.scrollToPosition(0)
    }

    override fun onCallFail() {
        super.onCallFail() // Calls the default Log.e
        runOnUiThread {
            Toast.makeText(
                this@MangaReaderActivity,
                "Error: Could not load chapter. Please check your connection or try again.",
                Toast.LENGTH_LONG
            ).show()
            // Optionally, re-enable navigation buttons or offer a retry mechanism here
            // For now, just showing the toast as per requirements.
            // If buttons were hidden before the call, they might need to be shown again.
            // Example:
            // mangaReaderBinding.nextChapter.show()
            // mangaReaderBinding.previousChapter.show()
            // mangaReaderBinding.goBack.show()
        }
    }
}