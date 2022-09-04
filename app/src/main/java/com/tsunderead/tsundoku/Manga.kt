package com.tsunderead.tsundoku

var mangaList = mutableListOf<Manga>()

data class Manga (
    var cover: Int,
    var author: String,
    var title: String,
    val id: Int? = mangaList.size
)