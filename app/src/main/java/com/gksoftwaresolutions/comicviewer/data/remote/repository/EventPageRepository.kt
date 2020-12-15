package com.gksoftwaresolutions.comicviewer.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterEventView
import com.gksoftwaresolutions.comicviewer.data.ServiceGenerator
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.EventDataSource
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.EventDataSourceFactory
import com.gksoftwaresolutions.comicviewer.model.Event
import com.gksoftwaresolutions.comicviewer.model.NetworkState
import com.gksoftwaresolutions.comicviewer.util.Common
import io.reactivex.disposables.CompositeDisposable

class EventPageRepository {
    companion object {
        private const val TAG = "EventPageRepository"
    }

    private val client = ServiceGenerator.createService(MarvelClient::class.java)

    lateinit var eventPageList: LiveData<PagedList<AdapterEventView>>
    private lateinit var eventDataSourceFactory: EventDataSourceFactory<AdapterEventView>

    fun fetchEventList(
        disposable: CompositeDisposable,
        converter: (Event) -> AdapterEventView
    ): LiveData<PagedList<AdapterEventView>> {
        eventDataSourceFactory = EventDataSourceFactory(client, disposable, converter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Common.POST_PER_PAGE)
            .build()
        eventPageList = LivePagedListBuilder(eventDataSourceFactory, config).build()
        return eventPageList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<EventDataSource<AdapterEventView>, NetworkState>(
            eventDataSourceFactory.eventResultDataSource,
            EventDataSource<AdapterEventView>::networkState
        )
    }
}