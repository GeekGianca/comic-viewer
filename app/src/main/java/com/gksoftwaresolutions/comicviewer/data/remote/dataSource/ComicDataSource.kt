package com.gksoftwaresolutions.comicviewer.data.remote.dataSource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.gksoftwaresolutions.comicviewer.data.remote.MarvelClient
import com.gksoftwaresolutions.comicviewer.model.Comic
import com.gksoftwaresolutions.comicviewer.model.NetworkState
import com.gksoftwaresolutions.comicviewer.util.Common
import com.xwray.groupie.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ComicDataSource<T : Item<*>>(
    private val client: MarvelClient,
    private val disposable: CompositeDisposable,
    private val converter: (Comic) -> T
) : PageKeyedDataSource<Int, T>() {

    companion object {
        private const val TAG = "ComicDataSource"
    }

    private var page = Common.PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        networkState.postValue(NetworkState.LOADING)
        Common.PARAMS["limit"] = "${Common.POST_PER_PAGE}"
        Common.PARAMS["offset"] = "$page"
        disposable.add(
            client.comics(Common.PARAMS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.onResult(
                        it.data.results.map(converter),
                        null,
                        page + Common.POST_PER_PAGE
                    )
                    if (it.data.count == 0) {
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
            client.comics(Common.PARAMS)
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