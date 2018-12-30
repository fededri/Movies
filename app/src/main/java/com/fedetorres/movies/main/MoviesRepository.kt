package com.fedetorres.movies.main


import com.fedetorres.movies.database.MovieDao
import com.fedetorres.movies.database.entities.Movie
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.net.UnknownHostException
import javax.inject.Inject

class MoviesRepository @Inject constructor(val moviesApiModel: MoviesApiModel, val movieDao: MovieDao) {



    fun getMoviesFromDb(): Observable<List<Movie>>{
        return movieDao.getMovies().toObservable()
    }

    suspend fun getMovies(
        list: Int = 1,
        page: Int = 1,
        sortBy: String? = null
    ): List<Movie>? {
        val movies = moviesApiModel.getMoviesDeferred(list, page, sortBy!!)
        if (movies != null) {
            return movies
        } else {
            //something was wrong
            return CoroutineScope(Dispatchers.Default).async{
                movieDao.getMoviesCoroutine()
            }.await()
        }
    }

}