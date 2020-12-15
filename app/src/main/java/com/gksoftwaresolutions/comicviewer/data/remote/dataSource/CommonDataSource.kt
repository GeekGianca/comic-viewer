package com.gksoftwaresolutions.comicviewer.data.remote.dataSource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gksoftwaresolutions.comicviewer.data.ServiceGenerator
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.model.*
import com.gksoftwaresolutions.comicviewer.util.Common
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CommonDataSource {
    companion object {
        private const val TAG = "CommonDataSource"
    }

    private val client = ServiceGenerator.createService(MarvelClient::class.java)
    private val disposable = CompositeDisposable()
    private val mutableComicData = MutableLiveData<Comic>()
    private val mutableCharacterData = MutableLiveData<Character>()
    private val mutableSeriesData = MutableLiveData<Serie>()
    private val mutableEventData = MutableLiveData<Event>()
    private val mutableCreatorData = MutableLiveData<Creator>()
    private val mutableErrorData = MutableLiveData<String>()

    //Lists
    private val mutableComicListData = MutableLiveData<List<Comic>>()
    private val mutableCharacterListData = MutableLiveData<List<Character>>()

    fun observableComicData(): LiveData<Comic> {
        return mutableComicData
    }

    fun observableCharacterData(): LiveData<Character> {
        return mutableCharacterData
    }

    fun observableSeriesData(): LiveData<Serie> {
        return mutableSeriesData
    }

    fun observableEventData(): LiveData<Event> {
        return mutableEventData
    }

    fun observableCreatorData(): LiveData<Creator> {
        return mutableCreatorData
    }

    fun observableComicListData(): LiveData<List<Comic>> {
        return mutableComicListData
    }

    fun observableCharacterListData(): LiveData<List<Character>> {
        return mutableCharacterListData
    }

    fun observableErrorData(): LiveData<String> {
        return mutableErrorData
    }

    fun findComicById(comicId: Int) {
        disposable.add(
            client.comic(comicId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseGenericData<List<Comic>>>() {
                    override fun onSuccess(t: ResponseGenericData<List<Comic>>) {
                        mutableComicData.value = t.data.results[0]
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, e.message, e)
                        mutableErrorData.value = Common.attachErrorHttp(e)
                    }

                })
        )
    }

    fun findCharacterById(characterId: Int) {
        disposable.add(
            client.character(characterId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseGenericData<List<Character>>>() {
                    override fun onSuccess(t: ResponseGenericData<List<Character>>) {
                        mutableCharacterData.value = t.data.results[0]
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, e.message, e)
                        mutableErrorData.value = Common.attachErrorHttp(e)
                    }

                })
        )
    }

    fun findSerieById(serieId: Int) {
        disposable.add(
            client.serie(serieId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseGenericData<List<Serie>>>() {
                    override fun onSuccess(t: ResponseGenericData<List<Serie>>) {
                        mutableSeriesData.value = t.data.results[0]
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, e.message, e)
                        mutableErrorData.value = Common.attachErrorHttp(e)
                    }

                })
        )
    }

    fun findEventById(eventId: Int) {
        disposable.add(
            client.event(eventId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseGenericData<List<Event>>>() {
                    override fun onSuccess(t: ResponseGenericData<List<Event>>) {
                        mutableEventData.value = t.data.results[0]
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, e.message, e)
                        mutableErrorData.value = Common.attachErrorHttp(e)
                    }

                })
        )
    }

    fun findCreatorById(creatorId: Int) {
        disposable.add(
            client.creator(creatorId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseGenericData<List<Creator>>>() {
                    override fun onSuccess(t: ResponseGenericData<List<Creator>>) {
                        mutableCreatorData.value = t.data.results[0]
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, e.message, e)
                        mutableErrorData.value = Common.attachErrorHttp(e)
                    }

                })
        )
    }

    fun findCharactersByComic(comicId: Int) {
        disposable.add(
            client.comicWithCharacters(comicId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableCharacterListData.value = it.data.results
                }, {
                    Log.d(TAG, it.message, it)
                    mutableErrorData.value = Common.attachErrorHttp(it)
                })
        )
    }

    fun findComicsByCharacters(characterId: Int) {
        disposable.add(
            client.characterWithComics(characterId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableComicListData.value = it.data.results
                }, {
                    Log.d(TAG, it.message, it)
                    mutableErrorData.value = Common.attachErrorHttp(it)
                })
        )
    }

    fun findComicsByCreator(creatorId: Int) {
        disposable.add(
            client.creatorWithComics(creatorId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableComicListData.value = it.data.results
                }, {
                    Log.d(TAG, it.message, it)
                    mutableErrorData.value = Common.attachErrorHttp(it)
                })
        )
    }

    fun findComicsByEvent(eventId: Int) {
        disposable.add(
            client.eventWithComics(eventId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableComicListData.value = it.data.results
                }, {
                    Log.d(TAG, it.message, it)
                    mutableErrorData.value = Common.attachErrorHttp(it)
                })
        )
    }

    fun findCharactersBySeries(seriesId: Int) {
        disposable.add(
            client.serieWithCharacters(seriesId,Common.CLEAN_PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableCharacterListData.value = it.data.results
                }, {
                    Log.d(TAG, it.message, it)
                    mutableErrorData.value = Common.attachErrorHttp(it)
                })
        )
    }

    fun clear() {
        disposable.clear()
    }

}