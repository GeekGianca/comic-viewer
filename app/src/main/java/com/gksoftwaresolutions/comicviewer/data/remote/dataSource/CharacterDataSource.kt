package com.gksoftwaresolutions.comicviewer.data.remote.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.model.Character
import com.gksoftwaresolutions.comicviewer.model.NetworkState
import com.gksoftwaresolutions.comicviewer.util.Common
import com.gksoftwaresolutions.comicviewer.util.Common.PAGE
import com.xwray.groupie.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterDataSource<T : Item<*>>(
    private val client: MarvelClient,
    private val disposable: CompositeDisposable,
    private val converter: (Character) -> T
) : PageKeyedDataSource<Int, T>() {

    companion object {
        private const val TAG = "CharacterDataSource"
    }

    private var page = PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        Log.d(TAG, "Initial search")
        Common.PARAMS["limit"] = "${Common.POST_PER_PAGE}"
        Common.PARAMS["offset"] = "$page"
        networkState.postValue(NetworkState.LOADING)
        disposable.add(
            client.characters(Common.PARAMS)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(
                        it.data.results.map(converter),
                        null,
                        page + Common.POST_PER_PAGE
                    )
                    if (it.data.results.isEmpty()) {
                        networkState.postValue(NetworkState.EMPTY)
                    } else {
                        networkState.postValue(NetworkState.LOADED)
                    }
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e(TAG, it.message, it)
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        networkState.postValue(NetworkState.LOADING)
        Common.PARAMS["offset"] = "${params.key}"
        disposable.add(
            client.characters(Common.PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.data.total >= params.key) {
                        callback.onResult(
                            it.data.results.map(converter),
                            params.key + Common.POST_PER_PAGE
                        )
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e(TAG, it.message, it)
                })
        )
    }
}