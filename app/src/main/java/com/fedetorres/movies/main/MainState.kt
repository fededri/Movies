package com.fedetorres.movies.main

import com.fedetorres.movies.network.Movie

data class MainState(
    val loading: Boolean = false,
    val error: String? = null,
    val selectedCategory: CATEGORY? = null,
    val movies: List<Movie>? = null
)


enum class CATEGORY {
    TOP_RATED,
    UPCOMING,
    POPULAR
}