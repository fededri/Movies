package com.fedetorres.movies.dagger

import com.fedetorres.movies.main.MainActivity
import com.fedetorres.movies.main.movieDetail.MovieDetailActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun detailsActicity(): MovieDetailActivity

}