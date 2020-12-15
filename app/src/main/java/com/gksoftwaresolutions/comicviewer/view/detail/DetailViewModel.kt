package com.gksoftwaresolutions.comicviewer.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gksoftwaresolutions.comicviewer.data.local.entities.ItemMarvel
import com.gksoftwaresolutions.comicviewer.data.local.repository.LocalDatabaseRepository
import com.gksoftwaresolutions.comicviewer.data.remote.repository.CommonRepository
import com.gksoftwaresolutions.comicviewer.model.*
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val repoCommon: CommonRepository,
    private val repoLocal: LocalDatabaseRepository
) : ViewModel() {

    fun observableComic(): LiveData<Comic> {
        return repoCommon.observerComic
    }

    fun observableCharacter(): LiveData<Character> {
        return repoCommon.observerCharacter
    }

    fun observableSeries(): LiveData<Serie> {
        return repoCommon.observerSeries
    }

    fun observableEvent(): LiveData<Event> {
        return repoCommon.observerEvent
    }

    fun observableCreator(): LiveData<Creator> {
        return repoCommon.observerCreator
    }

    fun observableListComics(): LiveData<List<Comic>> {
        return repoCommon.observerListComics
    }

    fun observableListCharacters(): LiveData<List<Character>> {
        return repoCommon.observerListCharacters
    }

    fun observableError(): LiveData<String> {
        return repoCommon.observerError
    }

    fun observableAddFavorite(): LiveData<ItemMarvel> {
        return repoLocal.observableItem()
    }

    fun observableRemoveFavorite(): LiveData<String> {
        return repoLocal.observableSuccessRemove()
    }

    fun findComic(comicId: Int) {
        repoCommon.findComic(comicId)
    }

    fun findCharacter(characterId: Int) {
        repoCommon.findCharacter(characterId)
    }

    fun findSeries(seriesId: Int) {
        repoCommon.findSeries(seriesId)
    }

    fun findEvent(eventId: Int) {
        repoCommon.findEvent(eventId)
    }

    fun findCreator(creatorId: Int) {
        repoCommon.findCreator(creatorId)
    }

    fun findCharactersByComic(comicId: Int) {
        repoCommon.findCharactersByComic(comicId)
    }

    fun findComicsByCharacters(characterId: Int) {
        repoCommon.findComicsByCharacters(characterId)
    }

    fun findComicsByCreator(creatorId: Int) {
        repoCommon.findComicsByCreator(creatorId)
    }

    fun findComicsByEvent(eventId: Int) {
        repoCommon.findComicsByEvent(eventId)
    }

    fun findCharactersBySeries(seriesId: Int) {
        repoCommon.findCharactersBySeries(seriesId)
    }

    fun findItem(id: Int) {
        repoLocal.findItem(id);
    }

    fun addFavorites(item: ItemMarvel) {
        repoLocal.addFavoriteItem(item)
    }

    fun removeFavorites(id:Int){
        repoLocal.removeFromFavorites(id)
    }

    override fun onCleared() {
        super.onCleared()
        repoCommon.clear()
    }
}