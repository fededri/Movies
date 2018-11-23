package com.fedetorres.movies.main


import com.fedetorres.movies.database.MovieDao
import com.fedetorres.movies.database.entities.Movie
import io.reactivex.Observable
import javax.inject.Inject

open class MoviesRepository @Inject constructor(val moviesApiModel: MoviesApiModel, val movieDao: MovieDao) {


    fun getMoviesFromDb(keyword: String? = null): Observable<List<Movie>> {
        return movieDao.getMovies().toObservable()
    }


    fun getMoviesFromWebService(
        list: Int = 1,
        page: Int = 1,
        sortBy: String? = null
    ): Observable<List<Movie>> {
        return moviesApiModel.getMovies(list, page, sortBy)
            .doOnNext { movieDao.insertAll(it) }
    }

}