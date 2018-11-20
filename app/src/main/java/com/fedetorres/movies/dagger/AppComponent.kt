package com.fedetorres.movies.dagger

import com.fedetorres.movies.BaseActivity
import com.fedetorres.movies.main.MainActivity
import dagger.Component

@Component(modules = arrayOf(ContextModule::class, MoviesModule::class) )
interface AppComponent {

    fun inject(activity: MainActivity)
}