package com.fedetorres.movies.dagger

import com.fedetorres.movies.database.DbMigration
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import javax.inject.Singleton

@Module
class RealmModule {

    val currentShemaVersion = 1L

    @Provides
    fun realm(): Realm {
        val config = RealmConfiguration.Builder()
            .schemaVersion(currentShemaVersion)
            .build()

        return Realm.getInstance(config)
    }


    @Provides
    fun realmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder()
            .schemaVersion(currentShemaVersion)
            .build()
    }


    @Provides
    fun migration(): DbMigration {
        return DbMigration()
    }


}