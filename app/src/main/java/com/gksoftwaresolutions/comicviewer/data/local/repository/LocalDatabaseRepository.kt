package com.gksoftwaresolutions.comicviewer.data.local.repository

import androidx.lifecycle.LiveData
import com.gksoftwaresolutions.comicviewer.data.local.dataSource.LocalDatabaseDataSource
import com.gksoftwaresolutions.comicviewer.data.local.entities.ItemMarvel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val dataSource: LocalDatabaseDataSource) {
    companion object {
        private const val TAG = "LocalDatabaseDataSource"
        private var INSTANCE: LocalDatabaseRepository? = null

        fun getInstance(ds: LocalDatabaseDataSource): LocalDatabaseRepository {
            if (INSTANCE == null)
                INSTANCE = LocalDatabaseRepository(ds)
            return INSTANCE!!
        }
    }

    fun observableItem(): LiveData<ItemMarvel> = dataSource.observableItem()

    fun observableListItem(): LiveData<List<ItemMarvel>> = dataSource.observableListItem()

    fun observableError(): LiveData<String> = dataSource.observableError()

    fun observableSuccess(): LiveData<String> = dataSource.observableSuccess()

    fun observableSuccessRemove(): LiveData<String> = dataSource.observableSuccessRemove()

    fun addFavoriteItem(item: ItemMarvel) {
        dataSource.addFavoriteItem(item)
    }

    fun removeFromFavorites(id: Int) {
        dataSource.removeFromFavorites(id)
    }

    fun findItem(id: Int) {
        dataSource.findItem(id)
    }

    fun findAllItems() {
        dataSource.findAllItems()
    }
}