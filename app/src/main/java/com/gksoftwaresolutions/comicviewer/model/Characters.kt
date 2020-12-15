package com.gksoftwaresolutions.comicviewer.model

data class Characters(
    val available: Int,
    val collectionURI: String,
    val items: List<Any>,
    val returned: Int
)