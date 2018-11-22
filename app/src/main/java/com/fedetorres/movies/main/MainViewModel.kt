package com.fedetorres.movies.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.fedetorres.movies.database.DbManager
import com.fedetorres.movies.network.ApiErrorParser
import com.fedetorres.movies.network.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val moviesModel: MoviesApiModel,
    private val dbManager: DbManager,
    private val apiErrorParser: ApiErrorParser

) :
    ViewModel() {

    companion object {
        val TAG = "MainViewModel"
    }

    private val data: MutableLiveData<MainState> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()


    init {
        data.postValue(MainState())
    }

    fun getData(): LiveData<MainState> {
        return data
    }


    fun getMovies(category: CATEGORY) {
        postState(data.value?.copy(selectedCategory = category, loading = true))

        val sort = getSortString(category)
        val disposable = moviesModel.getMovies(sortBy = sort)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::onMoviesObtained, this::errorGettingMovies)

        if (disposable != null)
            compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun goBack() {
        postState(state()?.copy(movies = null, selectedCategory = null, loading = false))
    }


    /***********************Private Methods*********************************************************/

    private fun getSortString(category: CATEGORY): String {
        return when (category) {
            CATEGORY.UPCOMING -> "release_date.asc"
            CATEGORY.TOP_RATED -> "vote_average.desc"
            CATEGORY.POPULAR -> "vote_average.desc" // TODO I dont find a better query for getting popular movies
        }
    }

    private fun onMoviesObtained(movies: List<Movie>) {
        Log.i(TAG, "Got movies")
        dbManager.saveMovies(movies)
        postState(state()?.copy(movies = movies, loading = false, error = null))
    }

    private fun errorGettingMovies(error: Throwable) {
        val message = apiErrorParser.parseError(error)
        postState(state()?.copy(loading = false, error = message))
    }


    private fun state(): MainState? = data.value

    private fun postState(state: MainState?) {
        data.postValue(state)
    }


    private fun setState(state: MainState?) {
        data.value = state
    }


}