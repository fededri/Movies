package com.fedetorres.movies.main

import android.util.Log
import com.fedetorres.movies.database.DbManager
import com.fedetorres.movies.network.GetMoviesResponse
import com.fedetorres.movies.network.Movie
import com.fedetorres.movies.network.MoviesApi
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesApiModel(private val api: MoviesApi) {

    private val authorizationBearer: String by lazy { "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMmRkMmZmZmY0YzM5ZTBmODAwNTE3MmE3YjFlODIzZCIsInN1YiI6IjViZjQ3NTFjYzNhMzY4MThhZTA5ZGVhMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.cerWt0TIs-ARuJU1iwsq8medj5IrhAMasBPhJpY9_2w" }
    private val contentType: String by lazy { "application/json;charset=utf-8" }


    companion object {
        val TAG = "MoviesApiModel"
    }

    fun getMovies(list: Int = 1, page: Int = 1, sortBy: String? = null): Single<List<Movie>> {
        return api.getMovies(
            type = contentType,
            authorization = authorizationBearer,
            list = list,
            sort = sortBy,
            page = page
        ).map {
            it.results

        }.map(this::formatPictureUrl)
    }


    private fun formatPictureUrl(movies: List<Movie>): List<Movie> {
        return movies.map {
            it.picture = "https://image.tmdb.org/t/p/w500${it.poster_path}"
            it
        }

    }
}