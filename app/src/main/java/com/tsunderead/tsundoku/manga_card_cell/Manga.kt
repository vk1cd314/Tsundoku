package com.tsunderead.tsundoku.manga_card_cell

var mangaList = arrayListOf<Manga>()

data class Manga (
    var cover: String,
    var author: String,
    var title: String,
    var mangaId: String
)