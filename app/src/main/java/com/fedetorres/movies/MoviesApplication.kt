package com.fedetorres.movies

import android.app.Activity
import android.app.Application
import com.fedetorres.movies.dagger.AppComponent
import com.fedetorres.movies.dagger.ContextModule
import com.fedetorres.movies.dagger.DaggerAppComponent
import dagger.android.*
import javax.inject.Inject


class MoviesApplication : DaggerApplication() {

    var component: AppComponent? = null


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerAppComponent.builder().contextModule(ContextModule(this))
            .build()


        component.inject(this)
        return component
    }

}