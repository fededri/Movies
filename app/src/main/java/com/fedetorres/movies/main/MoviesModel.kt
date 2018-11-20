package com.fedetorres.movies.main

import com.fedetorres.movies.network.GetMoviesResponse
import com.fedetorres.movies.network.MoviesApi
import io.reactivex.Single
import javax.inject.Inject

class MoviesModel(private val api: MoviesApi) {

    private val authorizationBearer: String by lazy { "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMmRkMmZmZmY0YzM5ZTBmODAwNTE3MmE3YjFlODIzZCIsInN1YiI6IjViZjQ3NTFjYzNhMzY4MThhZTA5ZGVhMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.cerWt0TIs-ARuJU1iwsq8medj5IrhAMasBPhJpY9_2w" }
    private val contentType: String by lazy { "application/json;charset=utf-8" }

    fun getMovies(list: Int = 1): Single<GetMoviesResponse> {
        return api.getMovies(
            type = contentType,
            authorization = authorizationBearer,
            list = list
        )
    }


}