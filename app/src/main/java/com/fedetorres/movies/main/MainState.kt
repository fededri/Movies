package com.fedetorres.movies.main

import com.fedetorres.movies.database.entities.Movie

data class MainState(
    val loading: Boolean = false,
    val error: String? = null,
    val selectedCategory: CATEGORY? = null,
    val movies: List<Movie>? = null,
    val keyword: String? = null,
    val currentPage: Int = 1
) {
    fun keyword(keyword: String?) = copy(keyword = keyword)

    fun loading(loading: Boolean) = copy(loading = loading)

    fun category(category: CATEGORY?) = copy(selectedCategory = category)

    fun movies(movies: List<Movie>?) = copy(movies = movies)

    fun error(error: String?) = copy(error = error)
}


enum class CATEGORY {
    TOP_RATED,
    UPCOMING,
    POPULAR
}