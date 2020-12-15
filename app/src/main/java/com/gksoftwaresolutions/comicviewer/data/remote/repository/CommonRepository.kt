package com.gksoftwaresolutions.comicviewer.data.remote.repository

import com.gksoftwaresolutions.comicviewer.data.remote.dataSource.CommonDataSource


class CommonRepository {
    private val commonDataSource = CommonDataSource()

    val observerComic = commonDataSource.observableComicData()
    val observerCharacter = commonDataSource.observableCharacterData()
    val observerSeries = commonDataSource.observableSeriesData()
    val observerEvent = commonDataSource.observableEventData()
    val observerCreator = commonDataSource.observableCreatorData()
    val observerListCharacters = commonDataSource.observableCharacterListData()
    val observerListComics = commonDataSource.observableComicListData()
    val observerError = commonDataSource.observableErrorData()

    fun findComic(comicId: Int) {
        commonDataSource.findComicById(comicId)
    }

    fun findCharacter(characterId: Int) {
        commonDataSource.findCharacterById(characterId)
    }

    fun findSeries(seriesId: Int) {
        commonDataSource.findSerieById(seriesId)
    }

    fun findEvent(eventId: Int) {
        commonDataSource.findEventById(eventId)
    }

    fun findCreator(creatorId: Int) {
        commonDataSource.findCreatorById(creatorId)
    }

    fun findCharactersByComic(comicId: Int) {
        commonDataSource.findCharactersByComic(comicId)
    }

    fun findComicsByCharacters(characterId: Int) {
        commonDataSource.findComicsByCharacters(characterId)
    }

    fun findComicsByCreator(creatorId: Int) {
        commonDataSource.findComicsByCreator(creatorId)
    }

    fun findComicsByEvent(eventId: Int) {
        commonDataSource.findComicsByEvent(eventId)
    }

    fun findCharactersBySeries(seriesId: Int) {
        commonDataSource.findCharactersBySeries(seriesId)
    }

    fun clear() {
        commonDataSource.clear()
    }

}