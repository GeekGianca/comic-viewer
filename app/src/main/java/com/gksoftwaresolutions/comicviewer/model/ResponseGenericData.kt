package com.gksoftwaresolutions.comicviewer.model

data class ResponseGenericData<T>(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: DataPagination<T>
)