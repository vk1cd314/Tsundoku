package com.tsunderead.tsundoku.offlinedb

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.tsunderead.tsundoku.history.MangaWithChapter
import com.tsunderead.tsundoku.manga_card_cell.Manga

class LibraryDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i("DB", "CREATED")
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME " +
                    "($COLUMN_MANGAID TEXT UNIQUE, $COLUMN_AUTHOR TEXT, " +
                    "$COLUMN_COVER TEXT, $COLUMN_TITLE TEXT, $COLUMN_LASTREAD TEXT, $COLUMN_LASTREADHASH TEXT, $COLUMN_TIMELASTREAD NUMERIC)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    @SuppressLint("Recycle", "Range")
    fun insertManga(manga: Manga) {
        val values = ContentValues()
        values.put(COLUMN_MANGAID, manga.mangaId)
        values.put(COLUMN_AUTHOR, manga.author)
        values.put(COLUMN_COVER, manga.cover)
        values.put(COLUMN_TITLE, manga.title)
        values.put(COLUMN_LASTREAD, "-1")
        values.put(COLUMN_LASTREADHASH, "-1")
        values.put(COLUMN_TIMELASTREAD, System.currentTimeMillis() / 1000)

        Log.i("Inserting Manga", manga.toString())

        if (isPresent(manga)) return

        val db = this.writableDatabase
        try {
            val success = db.insert(TABLE_NAME, null, values)
            if (Integer.parseInt("$success") != -1) run {
                Log.i("Adding", "Succeded")
            } else {
                Log.i("HUH", "WHY")
            }
        } catch (e: Exception) {
            Log.i("No", "Worketh")
        }
        db.close()
    }

    @SuppressLint("Recycle")
    fun isPresent(manga: Manga): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * from $TABLE_NAME WHERE ($COLUMN_MANGAID = '${manga.mangaId}')",
            null
        ) ?: return false
        if (cursor.count < 1) return false
        return true
    }

    fun updateManga(mangaId: String, manga: MangaWithChapter) {
        val values = ContentValues()
        values.put(COLUMN_MANGAID, mangaId)
        values.put(COLUMN_AUTHOR, manga.manga.author)
        values.put(COLUMN_COVER, manga.manga.cover)
        values.put(COLUMN_TITLE, manga.manga.title)
        values.put(COLUMN_LASTREAD, manga.chapter.chapterNumber.toString())
        values.put(COLUMN_LASTREADHASH, manga.chapter.chapterHash)
        values.put(COLUMN_TIMELASTREAD, System.currentTimeMillis() / 1000)

        val db = this.writableDatabase
        db.update(TABLE_NAME, values, "$COLUMN_MANGAID = ?", arrayOf(mangaId))
        db.close()
    }

    @SuppressLint("Recycle")
    fun deleteHistory() {
        val db = this.writableDatabase
        Log.i("Trying", "To Delete")
        db.rawQuery("UPDATE $TABLE_NAME SET $COLUMN_LASTREADHASH = -1", null)
    }

    fun deleteManga(mangaId: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_MANGAID = ?", arrayOf(mangaId))
        db.close()
    }

    fun getAllManga(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getOneManga(mangaId: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_MANGAID = ?", arrayOf(mangaId))
    }

    fun getAllMangaWithHistory(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE ($COLUMN_LASTREADHASH != -1) ORDER BY $COLUMN_TIMELASTREAD DESC",
            null
        )
    }

    companion object Constants {
        const val DATABASE_VERSION = 28
        const val DATABASE_NAME = "LibraryDBfile.db"
        const val TABLE_NAME = "LibraryManga"

        const val COLUMN_MANGAID = "mangaId"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_TITLE = "title"
        const val COLUMN_COVER = "cover"
        const val COLUMN_LASTREAD = "lastread"
        const val COLUMN_LASTREADHASH = "lastreadhash"
        const val COLUMN_TIMELASTREAD = "timelastread"
    }
}