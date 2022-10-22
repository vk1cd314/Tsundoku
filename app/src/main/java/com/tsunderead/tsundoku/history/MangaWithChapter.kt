package com.tsunderead.tsundoku.history

import com.tsunderead.tsundoku.chapter.Chapter
import com.tsunderead.tsundoku.manga_card_cell.Manga

data class MangaWithChapter(
    var manga: Manga,
    var chapter: Chapter
)
