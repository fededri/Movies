package com.fedetorres.movies.dagger

import android.arch.persistence.room.Room
import android.content.Context
import com.fedetorres.movies.database.Database
import dagger.Module
import dagger.Provides


@Module
class RoomModule {


    @Provides
    fun database(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "movies-database").build()
    }


    @Provides
    fun userDao(database: Database) = database.movieDao()
}