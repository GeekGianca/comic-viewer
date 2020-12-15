package com.gksoftwaresolutions.comicviewer.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterCharacterView
import com.gksoftwaresolutions.comicviewer.data.ServiceGenerator
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.CreatorDataSource
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.CreatorDataSourceFactory
import com.gksoftwaresolutions.comicviewer.model.Creator
import com.gksoftwaresolutions.comicviewer.model.NetworkState
import com.gksoftwaresolutions.comicviewer.util.Common
import io.reactivex.disposables.CompositeDisposable

class CreatorPageRepository {
    companion object {
        private const val TAG = "CreatorPageRepository"
    }

    private val client = ServiceGenerator.createService(MarvelClient::class.java)

    lateinit var creatorPageList: LiveData<PagedList<AdapterCharacterView>>
    private lateinit var creatorDataSourceFactory: CreatorDataSourceFactory<AdapterCharacterView>

    fun fetchCreatorList(
        disposable: CompositeDisposable,
        converter: (Creator) -> AdapterCharacterView
    ): LiveData<PagedList<AdapterCharacterView>> {
        creatorDataSourceFactory = CreatorDataSourceFactory(client, disposable, converter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Common.POST_PER_PAGE)
            .build()
        creatorPageList = LivePagedListBuilder(creatorDataSourceFactory, config).build()
        return creatorPageList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<CreatorDataSource<AdapterCharacterView>, NetworkState>(
            creatorDataSourceFactory.creatorResultDataSource,
            CreatorDataSource<AdapterCharacterView>::networkState
        )
    }
}