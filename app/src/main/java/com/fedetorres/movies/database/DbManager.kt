package com.fedetorres.movies.database

import com.fedetorres.movies.network.Movie
import io.reactivex.*
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

class DbManager @Inject constructor(val realmConfiguration: RealmConfiguration) {


    fun getRealm(): Realm {
        return Realm.getInstance(realmConfiguration)
    }


    fun saveMovies(movies: List<Movie>) {
        val realm = getRealm()
        realm.executeTransactionAsync { realm ->
            for (movie in movies) {
                realm.copyToRealmOrUpdate(movie)
            }
            realm.close()
        }
    }


    fun getMovie(id: Int?): Movie? {
        val realm = getRealm()
        val movie = realm.where(Movie::class.java).equalTo("id", id).findFirst()
        realm.close()
        return movie
    }

    fun getMovies(): List<Movie> {
        val realm = getRealm()
        val list = realm.where(Movie::class.java).findAll()
        realm.close()
        return list
    }

}