package com.gksoftwaresolutions.comicviewer.data.local.dao

import androidx.room.*
import com.gksoftwaresolutions.comicviewer.data.local.entities.ItemMarvel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class ItemMarvelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: ItemMarvel)

    @Query("SELECT * FROM item_marvel WHERE  id = :id LIMIT 1;")
    abstract fun selectItemById(id: Int): Flow<ItemMarvel>

    @Query("SELECT * FROM item_marvel;")
    abstract fun selectAllItems(): Flow<List<ItemMarvel>>

    fun selectItemByIdDistinctUntilChanged(id: Int) =
        selectItemById(id).distinctUntilChanged()

    fun selectAllItemsDistinctUntilChanged() =
        selectAllItems().distinctUntilChanged()

    @Query("DELETE FROM item_marvel WHERE id = :id;")
    abstract suspend fun delete(id: Int)
}