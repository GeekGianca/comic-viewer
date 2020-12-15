package com.gksoftwaresolutions.comicviewer.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterCharacterView
import com.gksoftwaresolutions.comicviewer.data.ServiceGenerator
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.CharacterDataSource
import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.CharacterDataSourceFactory
import com.gksoftwaresolutions.comicviewer.model.Character
import com.gksoftwaresolutions.comicviewer.model.NetworkState
import com.gksoftwaresolutions.comicviewer.util.Common
import io.reactivex.disposables.CompositeDisposable

class CharacterPageRepository {
    companion object {
        private const val TAG = "CharacterPageRepository"
    }

    private val client = ServiceGenerator.createService(MarvelClient::class.java)

    lateinit var characterPageList: LiveData<PagedList<AdapterCharacterView>>
    private lateinit var characterDataSourceFactory: CharacterDataSourceFactory<AdapterCharacterView>

    fun fetchCharacterList(
        disposable: CompositeDisposable,
        converter: (Character) -> AdapterCharacterView
    ): LiveData<PagedList<AdapterCharacterView>> {
        characterDataSourceFactory = CharacterDataSourceFactory(client, disposable, converter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Common.POST_PER_PAGE)
            .build()
        characterPageList = LivePagedListBuilder(characterDataSourceFactory, config).build()
        return characterPageList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<CharacterDataSource<AdapterCharacterView>, NetworkState>(
            characterDataSourceFactory.characterResultDataSource,
            CharacterDataSource<AdapterCharacterView>::networkState
        )
    }
}