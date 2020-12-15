package com.gksoftwaresolutions.comicviewer.component

import com.gksoftwaresolutions.comicviewer.view.detail.DetailActivity
import com.gksoftwaresolutions.comicviewer.view.home.HomeActivity
import com.gksoftwaresolutions.comicviewer.view.home.ui.favorite.FavoriteFragment
import com.gksoftwaresolutions.comicviewer.view.home.ui.home.HomeFragment
import com.gksoftwaresolutions.comicviewer.view.login.MainActivity
import dagger.Component

@Component(modules = [ViewModelFactoryModule::class])
interface ComicComponent {
    fun inject(inject: MainActivity)
    fun inject(inject: HomeActivity)
    fun inject(inject: HomeFragment)
    fun inject(inject: DetailActivity)
    fun inject(inject: FavoriteFragment)
}