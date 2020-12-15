package com.gksoftwaresolutions.comicviewer.view.home.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gksoftwaresolutions.comicviewer.data.local.entities.ItemMarvel
import com.gksoftwaresolutions.comicviewer.data.local.repository.LocalDatabaseRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repoLocal: LocalDatabaseRepository) :
    ViewModel() {

    fun observableAllItems(): LiveData<List<ItemMarvel>> {
        return repoLocal.observableListItem()
    }

    fun findAllItems() {
        repoLocal.findAllItems()
    }

}