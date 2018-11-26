package com.fedetorres.movies.main


import com.fedetorres.movies.database.MovieDao
import com.fedetorres.movies.database.entities.Movie
import io.reactivex.Observable
import io.reactivex.Single
import java.net.UnknownHostException
import javax.inject.Inject

open class MoviesRepository @Inject constructor(val moviesApiModel: MoviesApiModel, val movieDao: MovieDao) {


    fun getMovies(
        keyword: String? = null,
        sort: String? = null,
        callback: ((error: Throwable?) -> Unit)? = null
    ): Single<List<Movie>> {
        return Observable.concatArrayDelayError(getMoviesFromWebService(sortBy = sort), getMoviesFromDb(keyword))
            .materialize()
            .filter {
                if (it.isOnError) callback?.invoke(it.error)
                !it.isOnError
            }
            .dematerialize<List<Movie>>()
            .flatMap {
                Observable.fromIterable(it)
            }
            .toList()
    }

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