package com.fedetorres.movies.dagger

import com.fedetorres.movies.main.MoviesModel
import com.fedetorres.movies.network.MoviesApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class MoviesModule {

    @Provides
    fun moviesModel(api: MoviesApi): MoviesModel {
        return MoviesModel(api)
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