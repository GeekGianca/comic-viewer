package com.gksoftwaresolutions.comicviewer.model

data class DataPagination<T>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: T
)