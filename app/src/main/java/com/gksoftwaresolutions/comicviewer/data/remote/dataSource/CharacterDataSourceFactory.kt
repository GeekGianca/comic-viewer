package com.gksoftwaresolutions.comicviewer.data.remote.dataSource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.model.Character
import com.gksoftwaresolutions.comicviewer.model.Comic
import com.xwray.groupie.Item
import io.reactivex.disposables.CompositeDisposable

class CharacterDataSourceFactory<T : Item<*>>(
    private val client: MarvelClient,
    private val disposable: CompositeDisposable,
    private val converter: (Character) -> T
) : DataSource.Factory<Int, T>() {

    companion object {
        private const val TAG = "ComicDataSourceFactory"
    }

    var characterResultDataSource = MutableLiveData<CharacterDataSource<T>>()

    override fun create(): DataSource<Int, T> {
        val characterDataSource = CharacterDataSource(client, disposable, converter)
        characterResultDataSource.postValue(characterDataSource)
        return characterDataSource
    }

    fun refresh() {
        characterResultDataSource.value?.invalidate()
    }
}