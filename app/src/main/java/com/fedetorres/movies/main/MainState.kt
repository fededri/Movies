package com.fedetorres.movies.main

data class MainState(
    val loading: Boolean = false,
    val error: String? = null,
    val selectedCategory: CATEGORY? = null
)


enum class CATEGORY {
    TOP_RATED,
    UPCOMING,
    POPULAR
}