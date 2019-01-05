package com.fedetorres.movies.dagger

import com.fedetorres.movies.main.MainActivity
import com.fedetorres.movies.main.movieDetail.MovieDetailActivity
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton


@Singleton
@Component(
    modules = arrayOf(
        AndroidInjectionModule::class, ContextModule::class, MoviesModule::class,
        RoomModule::class,
        ActivityBuilder::class
    )
)
interface AppComponent : AndroidInjector<DaggerApplication> {


    fun inject(activity: MainActivity)
    fun inject(activity: MovieDetailActivity)

}