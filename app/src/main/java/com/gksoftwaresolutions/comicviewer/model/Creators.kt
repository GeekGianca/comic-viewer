package com.gksoftwaresolutions.comicviewer.model

data class Creators(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)