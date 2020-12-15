package com.gksoftwaresolutions.comicviewer.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterItemView
import com.gksoftwaresolutions.comicviewer.data.ServiceGenerator
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.ComicDataSource
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.ComicDataSourceFactory
import com.gksoftwaresolutions.comicviewer.model.Comic
import com.gksoftwaresolutions.comicviewer.model.NetworkState
import com.gksoftwaresolutions.comicviewer.util.Common.POST_PER_PAGE
import io.reactivex.disposables.CompositeDisposable

class ComicPageRepository() {
    companion object {
        private const val TAG = "ComicPageRepository"
    }

    private val client = ServiceGenerator.createService(MarvelClient::class.java)

    lateinit var comicPageList: LiveData<PagedList<AdapterItemView>>
    private lateinit var comicDataSourceFactory: ComicDataSourceFactory<AdapterItemView>

    fun fetchComicList(
        disposable: CompositeDisposable,
        converter: (Comic) -> AdapterItemView
    ): LiveData<PagedList<AdapterItemView>> {
        comicDataSourceFactory = ComicDataSourceFactory(client, disposable, converter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()
        comicPageList = LivePagedListBuilder(comicDataSourceFactory, config).build()
        return comicPageList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<ComicDataSource<AdapterItemView>, NetworkState>(
            comicDataSourceFactory.comicResultDataSource,
            ComicDataSource<AdapterItemView>::networkState
        )
    }
}