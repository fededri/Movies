package com.fedetorres.movies.main

import com.fedetorres.movies.database.entities.Movie


sealed class MainState{

    data class Loading(val loading : Boolean = false) : MainState()

    data class Error(val message: String)  : MainState()



    data class Movies(val movies: List<Movie>): MainState()


    data class Page(val page: Int) : MainState()

    object ShowButtons : MainState()

    object GoBack : MainState()

    object NoMoviesFoundOnSearch : MainState()
}


enum class CATEGORY {
    TOP_RATED,
    UPCOMING,
    POPULAR
}