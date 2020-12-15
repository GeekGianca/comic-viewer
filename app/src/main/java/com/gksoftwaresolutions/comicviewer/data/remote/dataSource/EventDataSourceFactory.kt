package com.gksoftwaresolutions.comicviewer.data.remote.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.model.Event
import com.xwray.groupie.Item
import io.reactivex.disposables.CompositeDisposable

class EventDataSourceFactory<T : Item<*>>(
    private val client: MarvelClient,
    private val disposable: CompositeDisposable,
    private val converter: (Event) -> T
) : DataSource.Factory<Int, T>() {

    companion object {
        private const val TAG = "CreatorDataSourceFactor"
    }

    var eventResultDataSource = MutableLiveData<EventDataSource<T>>()
    override fun create(): DataSource<Int, T> {
        val eventDataSource = EventDataSource(client, disposable, converter)
        eventResultDataSource.postValue(eventDataSource)
        return eventDataSource
    }
}