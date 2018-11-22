package com.fedetorres.movies.network

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


data class GetMoviesResponse(
    val average_rating: Double,
    val backdrop_path: String,
    val comments: Map<String, String?>,
    val created_by: CreatedBy,
    val description: String,
    val id: Int,
    val iso_3166_1: String,
    val iso_639_1: String,
    val name: String,
    val object_ids: Map<String, String?>,
    val page: Int,
    val poster_path: String,
    val `public`: Boolean,
    val results: List<Movie>,
    val revenue: Long,
    val runtime: Int,
    val sort_by: String,
    val total_pages: Int,
    val total_results: Int
)

data class CreatedBy(
    val gravatar_hash: String,
    val id: String,
    val name: String,
    val username: String
)

@RealmClass
open class Movie : RealmObject() {
    open var adult: Boolean? = null
    open var backdrop_path: String? = null
    open var genre_ids: RealmList<Int>? = null
    @PrimaryKey
    open var id: Int? = null
    open var media_type: String? = null
    open var original_language: String? = null
    open var original_title: String? = null
    open var overview: String? = null
    open var popularity: Double? = null
    open var poster_path: String? = null
    open var release_date: String? = null
    open var title: String? = null
    open var video: Boolean? = null
    open var vote_average: Double? = null
    open var vote_count: Int? = null
    open var picture: String? = null

}
