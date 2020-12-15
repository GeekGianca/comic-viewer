package com.gksoftwaresolutions.comicviewer.data.remote.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.model.Comic
import com.xwray.groupie.Item
import io.reactivex.disposables.CompositeDisposable

class ComicDataSourceFactory<T : Item<*>>(
    private val client: MarvelClient,
    private val disposable: CompositeDisposable,
    private val converter: (Comic) -> T
) : DataSource.Factory<Int, T>() {
    companion object {
        private const val TAG = "ComicDataSourceFactory"
    }

    var comicResultDataSource = MutableLiveData<ComicDataSource<T>>()

    override fun create(): DataSource<Int, T> {
        val comicDataSource = ComicDataSource(client, disposable, converter)
        comicResultDataSource.postValue(comicDataSource)
        return comicDataSource
    }

    fun refresh() {
        comicResultDataSource.value?.invalidate()
    }
}