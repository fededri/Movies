package com.fedetorres.movies.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.fedetorres.movies.database.entities.Movie

import io.reactivex.Single

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getMovies(): Single<List<Movie>>


    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): Movie


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>)


}