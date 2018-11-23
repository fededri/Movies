package com.fedetorres.movies.dagger

import com.fedetorres.movies.BaseActivity
import com.fedetorres.movies.main.MainActivity
import com.fedetorres.movies.main.movieDetail.MovieDetailActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(ContextModule::class, MoviesModule::class, RoomModule::class) )
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: MovieDetailActivity)
}