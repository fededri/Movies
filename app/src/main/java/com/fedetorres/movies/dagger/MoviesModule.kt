package com.fedetorres.movies.dagger


import com.fedetorres.movies.main.MainViewModel
import com.fedetorres.movies.main.MoviesApiModel
import com.fedetorres.movies.main.MoviesRepository
import com.fedetorres.movies.network.ApiErrorParser
import com.fedetorres.movies.network.MoviesApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class MoviesModule {


    @Provides
    @Named("mainThread")
    fun mainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Named("ioThread")
    fun ioScheduler() : Scheduler{
        return Schedulers.io()
    }

    @Singleton
    @Provides
    fun mainViewModel(
        repository: MoviesRepository,
        apiErrorParser: ApiErrorParser,
        @Named("ioThread") ioScheduler: Scheduler,
        @Named("mainThread") androidScheduler: Scheduler
    ): MainViewModel {
        return MainViewModel(repository, apiErrorParser,ioScheduler,androidScheduler)
    }

    @Provides
    fun moviesModel(api: MoviesApi): MoviesApiModel {
        return MoviesApiModel(api)
    }

    @Provides
    fun getMoviesApi(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }

    @Provides
    fun retrofit(httpClient: OkHttpClient, gsonConverter: GsonConverterFactory, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://api.themoviedb.org/4/")
            .addConverterFactory(gsonConverter)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun okHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }


    @Provides
    fun gson(): Gson {
        return GsonBuilder().create()
    }


    @Provides
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

}