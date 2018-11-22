package com.fedetorres.movies

import android.app.Application
import com.fedetorres.movies.dagger.AppComponent
import com.fedetorres.movies.dagger.ContextModule
import com.fedetorres.movies.dagger.DaggerAppComponent
import io.realm.Realm

class MoviesApplication : Application() {

    var component: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        component = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()


    }
}