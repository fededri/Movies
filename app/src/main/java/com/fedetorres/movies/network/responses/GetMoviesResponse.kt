package com.fedetorres.movies.network.responses

import com.fedetorres.movies.database.entities.Movie


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

/*



@Entity(tableName = "movies")
open class Movie {
    @PrimaryKey var id: Int = 0

    @ColumnInfo(name = "adult")
    var adult: Boolean? = null
    @ColumnInfo(name = "backdropPath")
    var backdrop_path: String? = null


    @Ignore
    var genre_ids: List<Int>? = null


    @ColumnInfo(name = "mediaType")
    var media_type: String? = null

    @ColumnInfo(name = "originalLanguage")
    var original_language: String? = null

    @ColumnInfo(name = "originalTitle")
    var original_title: String? = null

    @ColumnInfo(name = "overview")
    var overview: String? = null

    @ColumnInfo(name = "popularity")
    var popularity: Double? = null

    @ColumnInfo(name = "posterPath")
    var poster_path: String? = null

    @ColumnInfo(name = "releaseDate")
    var release_date: String? = null

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "video")
    var video: Boolean? = null

    @ColumnInfo(name = "voteAverage")
    var vote_average: Double? = null

    @ColumnInfo(name = "voteCount")
    var vote_count: Int? = null

    @ColumnInfo(name = "picture")
    var picture: String? = null


}
 */

