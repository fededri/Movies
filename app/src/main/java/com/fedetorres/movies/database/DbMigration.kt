package com.fedetorres.movies.database

import io.realm.DynamicRealm
import io.realm.RealmMigration

class DbMigration : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        var old = oldVersion

        //migrate from version 1 to 2
        if (old == 1L) {

            old++
        }
    }

}