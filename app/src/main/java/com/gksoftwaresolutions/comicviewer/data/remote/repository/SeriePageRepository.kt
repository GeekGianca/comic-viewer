package com.gksoftwaresolutions.comicviewer.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterSeriesView
import com.gksoftwaresolutions.comicviewer.data.ServiceGenerator
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.SeriesDataSource
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.SeriesDataSourceFactory
import com.gksoftwaresolutions.comicviewer.model.NetworkState
import com.gksoftwaresolutions.comicviewer.model.Serie
import com.gksoftwaresolutions.comicviewer.util.Common
import io.reactivex.disposables.CompositeDisposable

class SeriePageRepository {
    companion object {
        private const val TAG = "EventPageRepository"
    }

    private val client = ServiceGenerator.createService(MarvelClient::class.java)

    lateinit var seriePageList: LiveData<PagedList<AdapterSeriesView>>
    private lateinit var serieDataSourceFactory: SeriesDataSourceFactory<AdapterSeriesView>

    fun fetchSerieList(
        disposable: CompositeDisposable,
        converter: (Serie) -> AdapterSeriesView
    ): LiveData<PagedList<AdapterSeriesView>> {
        serieDataSourceFactory = SeriesDataSourceFactory(client, disposable, converter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Common.POST_PER_PAGE)
            .build()
        seriePageList = LivePagedListBuilder(serieDataSourceFactory, config).build()
        return seriePageList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<SeriesDataSource<AdapterSeriesView>, NetworkState>(
            serieDataSourceFactory.seriesResultDataSource,
            SeriesDataSource<AdapterSeriesView>::networkState
        )
    }
}