package com.fedetorres.movies.network


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
    val object_ids: Map<String,String?>,
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

data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val media_type: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)
