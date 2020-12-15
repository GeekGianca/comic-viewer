package com.gksoftwaresolutions.comicviewer.data.remote.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.model.Event
import com.gksoftwaresolutions.comicviewer.model.Serie
import com.xwray.groupie.Item
import io.reactivex.disposables.CompositeDisposable

class SeriesDataSourceFactory <T : Item<*>>(
    private val client: MarvelClient,
    private val disposable: CompositeDisposable,
    private val converter: (Serie) -> T
) : DataSource.Factory<Int, T>() {

    companion object {
        private const val TAG = "SeriesDataSourceFactory"
    }

    var seriesResultDataSource = MutableLiveData<SeriesDataSource<T>>()
    override fun create(): DataSource<Int, T> {
        val seriesDataSource = SeriesDataSource(client, disposable, converter)
        seriesResultDataSource.postValue(seriesDataSource)
        return seriesDataSource
    }
}