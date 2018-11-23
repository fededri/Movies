package com.fedetorres.movies.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.fedetorres.movies.database.entities.Movie

@Database(entities = arrayOf(Movie::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}