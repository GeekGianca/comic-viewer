package com.gksoftwaresolutions.comicviewer

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.gksoftwaresolutions.comicviewer.component.ComicComponent
import com.gksoftwaresolutions.comicviewer.component.DaggerComicComponent
import com.gksoftwaresolutions.comicviewer.component.ViewModelFactoryModule
import com.gksoftwaresolutions.comicviewer.data.local.DatabaseConfig

class ComicApp : Application() {
    lateinit var appComponent: ComicComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerComicComponent
            .builder()
            .viewModelFactoryModule(ViewModelFactoryModule(this, DatabaseConfig.getInstance(this)))
            .build()
        Fresco.initialize(this)
    }
}