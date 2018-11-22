package com.fedetorres.movies.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("list/{listId}")
    fun getMovies(
        @Header("Content-Type") type: String,
        @Header("Authorization") authorization: String,
        @Path("listId") list: Int,
        @Query("page") page: Int? = null,
        @Query("sort_by") sort: String? = null
    ): Single<GetMoviesResponse>




}