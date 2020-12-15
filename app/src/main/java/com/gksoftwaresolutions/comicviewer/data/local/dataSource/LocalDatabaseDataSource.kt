package com.gksoftwaresolutions.comicviewer.data.local.dataSource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gksoftwaresolutions.comicviewer.data.local.DatabaseConfig
import com.gksoftwaresolutions.comicviewer.data.local.entities.ItemMarvel
import com.gksoftwaresolutions.comicviewer.model.Comic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseDataSource @Inject constructor(private val database: DatabaseConfig) {

    private val mutableComic = MutableLiveData<ItemMarvel>()
    private val mutableComicList = MutableLiveData<List<ItemMarvel>>()
    private val mutableError = MutableLiveData<String>()
    private val mutableSuccess = MutableLiveData<String>()
    private val mutableSuccessRemove = MutableLiveData<String>()

    companion object {
        private const val TAG = "LocalDatabaseDataSource"
        private var INSTANCE: LocalDatabaseDataSource? = null

        fun getInstance(config: DatabaseConfig): LocalDatabaseDataSource {
            if (INSTANCE == null)
                INSTANCE = LocalDatabaseDataSource(config)
            return INSTANCE!!
        }
    }

    fun observableItem(): LiveData<ItemMarvel> = mutableComic

    fun observableListItem(): LiveData<List<ItemMarvel>> = mutableComicList

    fun observableError(): LiveData<String> = mutableError

    fun observableSuccess(): LiveData<String> = mutableSuccess

    fun observableSuccessRemove(): LiveData<String> = mutableSuccessRemove

    fun addFavoriteItem(item: ItemMarvel) {
        val itemDao = database.itemMarvelDao()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                itemDao.insert(item)
                mutableSuccess.postValue("Was successfully added to favorites")
            } catch (e: Exception) {
                mutableError.postValue("Failed to add to favorites")
            }
        }
    }

    fun removeFromFavorites(id: Int) {
        val itemDao = database.itemMarvelDao()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                itemDao.delete(id)
                mutableSuccessRemove.postValue("Removed from favorites successfully")
            } catch (e: Exception) {
                mutableError.postValue("Failed to remove from favorites")
            }
        }
    }

    fun findItem(id: Int) {
        val itemDao = database.itemMarvelDao()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                itemDao.selectItemByIdDistinctUntilChanged(id).collect {
                    mutableComic.postValue(it)
                }
            } catch (e: Exception) {
                mutableError.postValue("Failed")
            }
        }
    }

    fun findAllItems() {
        val itemDao = database.itemMarvelDao()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                itemDao.selectAllItems().collect {
                    mutableComicList.postValue(it)
                }
            } catch (e: Exception) {
                mutableError.postValue("Failed")
            }
        }
    }
}