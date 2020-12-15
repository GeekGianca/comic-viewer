package com.gksoftwaresolutions.comicviewer.component

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.data.local.DatabaseConfig
import com.gksoftwaresolutions.comicviewer.data.local.dataSource.LocalDatabaseDataSource
import com.gksoftwaresolutions.comicviewer.data.local.repository.LocalDatabaseRepository
import com.gksoftwaresolutions.comicviewer.data.remote.repository.*
import com.gksoftwaresolutions.comicviewer.util.Common
import com.gksoftwaresolutions.comicviewer.view.detail.DetailViewModel
import com.gksoftwaresolutions.comicviewer.view.home.ui.favorite.FavoriteViewModel
import com.gksoftwaresolutions.comicviewer.view.home.ui.home.HomeViewModel
import com.gksoftwaresolutions.comicviewer.view.login.MainViewModel
import dagger.Module
import dagger.Provides
import kotlin.random.Random

@Module
class ViewModelFactoryModule(private val context: Context, private val database: DatabaseConfig) :
    ViewModelProvider.Factory {

    companion object {
        private const val TAG = "ViewModelFactoryModule"
    }

    @Provides
    fun provideContext() = context

    @Provides
    fun provideComicRepository() = ComicPageRepository()

    @Provides
    fun provideCharacterRepository() = CharacterPageRepository()

    @Provides
    fun provideCreatorRepository() = CreatorPageRepository()

    @Provides
    fun provideEventRepository() = EventPageRepository()

    @Provides
    fun provideSerieRepository() = SeriePageRepository()

    @Provides
    fun provideCommonRepository() = CommonRepository()

    @Provides
    fun provideLocalDatabase(): DatabaseConfig = database

    @Provides
    fun provideLocalDataSource() = LocalDatabaseDataSource(provideLocalDatabase())

    @Provides
    fun provideLocalRepository() = LocalDatabaseRepository(provideLocalDataSource())

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel() as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    provideComicRepository(),
                    provideCharacterRepository(),
                    provideCreatorRepository(),
                    provideEventRepository(),
                    provideSerieRepository(),
                    provideCommonRepository(),
                    provideLocalRepository()
                ) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(provideCommonRepository(), provideLocalRepository()) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(provideLocalRepository()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}