package com.gksoftwaresolutions.comicviewer.data.remote.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.model.Creator
import com.xwray.groupie.Item
import io.reactivex.disposables.CompositeDisposable

class CreatorDataSourceFactory<T : Item<*>>(
    private val client: MarvelClient,
    private val disposable: CompositeDisposable,
    private val converter: (Creator) -> T
) : DataSource.Factory<Int, T>() {
    companion object {
        private const val TAG = "CreatorDataSourceFactor"
    }

    var creatorResultDataSource = MutableLiveData<CreatorDataSource<T>>()

    override fun create(): DataSource<Int, T> {
        val creatorDataSource = CreatorDataSource(client, disposable, converter)
        creatorResultDataSource.postValue(creatorDataSource)
        return creatorDataSource
    }

    fun refresh() {
        creatorResultDataSource.value?.invalidate()
    }
}