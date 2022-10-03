package com.tsunderead.tsundoku

var mangaList = arrayListOf<Manga>()

data class Manga (
    var cover: String,
    var author: String,
    var title: String,
    val id: Int? = mangaList.size
)