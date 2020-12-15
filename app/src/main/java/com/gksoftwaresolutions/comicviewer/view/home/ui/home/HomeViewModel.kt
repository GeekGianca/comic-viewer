package com.gksoftwaresolutions.comicviewer.view.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterCharacterView
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterEventView
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterItemView
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterSeriesView
import com.gksoftwaresolutions.comicviewer.data.local.entities.ItemMarvel
import com.gksoftwaresolutions.comicviewer.data.local.repository.LocalDatabaseRepository
import com.gksoftwaresolutions.comicviewer.data.remote.repository.*
import com.gksoftwaresolutions.comicviewer.model.*
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repo: ComicPageRepository,
    private val repoCharacter: CharacterPageRepository,
    private val repoCreator: CreatorPageRepository,
    private val repoEvent: EventPageRepository,
    private val repoSeries: SeriePageRepository,
    private val repoComm: CommonRepository,
    private val repoLocal: LocalDatabaseRepository
) : ViewModel() {
    private val disposable = CompositeDisposable()

    //Fetch status loading
    val networkObservable: LiveData<NetworkState> by lazy {
        repo.getNetworkState()
    }

    //Fetch status loading
    val networkCharacterObservable: LiveData<NetworkState> by lazy {
        repoCharacter.getNetworkState()
    }

    //Fetch creator status loading
    val networkCreatorObservable: LiveData<NetworkState> by lazy {
        repoCreator.getNetworkState()
    }

    //Fetch event status loading
    val networkEventObservable: LiveData<NetworkState> by lazy {
        repoEvent.getNetworkState()
    }

    //Fetch series status loading
    val networkSeriesObservable: LiveData<NetworkState> by lazy {
        repoSeries.getNetworkState()
    }

    //Fetch list Events
    val eventsObservableList: LiveData<PagedList<AdapterEventView>> by lazy {
        repoEvent.fetchEventList(disposable, ::converterCreator)
    }

    //Fetch list Characters
    val charactersObservableList: LiveData<PagedList<AdapterCharacterView>> by lazy {
        repoCharacter.fetchCharacterList(disposable, ::converterCharacter)
    }

    //Fetch list with lazy loan
    val comicObservableList: LiveData<PagedList<AdapterItemView>> by lazy {
        repo.fetchComicList(disposable, ::converter)
    }

    //Fetch list creator with lazy loan
    val creatorObservableList: LiveData<PagedList<AdapterCharacterView>> by lazy {
        repoCreator.fetchCreatorList(disposable, ::converterCreator)
    }

    //Fetch list series with lazy loan
    val seriesObservableList: LiveData<PagedList<AdapterSeriesView>> by lazy {
        repoSeries.fetchSerieList(disposable, ::converterSeries)
    }

    fun findRandomComic(id: Int) {
        repoComm.findComic(id)
        selectItem(id)
    }

    fun observableComicRandom(): LiveData<Comic> {
        return repoComm.observerComic
    }

    private fun converter(p: Comic): AdapterItemView = AdapterItemView(
        p.id,
        p.format,
        p.title,
        String.format("%s.%s", p.thumbnail.path, p.thumbnail.extension)
    )

    private fun converterCharacter(p: Character): AdapterCharacterView = AdapterCharacterView(
        p.id,
        p.name,
        String.format("%s.%s", p.thumbnail.path, p.thumbnail.extension)
    )

    private fun converterCreator(p: Creator): AdapterCharacterView = AdapterCharacterView(
        p.id,
        p.fullName,
        String.format("%s.%s", p.thumbnail.path, p.thumbnail.extension)
    )

    private fun converterCreator(p: Event): AdapterEventView = AdapterEventView(
        p.id,
        p.urls,
        String.format("%s.%s", p.thumbnail.path, p.thumbnail.extension)
    )

    private fun converterSeries(p: Serie): AdapterSeriesView = AdapterSeriesView(
        p.id,
        p.startYear,
        p.endYear,
        p.type,
        p.rating,
        String.format("%s.%s", p.thumbnail.path, p.thumbnail.extension)
    )

    //Local requests

    fun observableItem(): LiveData<ItemMarvel> = repoLocal.observableItem()

    fun observableSuccess(): LiveData<String> = repoLocal.observableSuccess()

    fun observableError(): LiveData<String> = repoLocal.observableError()

    fun observableSuccessRemove(): LiveData<String> = repoLocal.observableSuccessRemove()

    fun addFavorites(item: ItemMarvel) {
        repoLocal.addFavoriteItem(item)
    }

    fun removeFavorites(id: Int) {
        repoLocal.removeFromFavorites(id)
    }

    private fun selectItem(id: Int) {
        repoLocal.findItem(id)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}