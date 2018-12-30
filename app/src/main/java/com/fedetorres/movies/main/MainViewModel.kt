package com.fedetorres.movies.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.fedetorres.movies.network.ApiErrorParser
import com.fedetorres.movies.database.entities.Movie
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

open class MainViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val apiErrorParser: ApiErrorParser,
    @Named("ioThread") private val ioScheduler: Scheduler,
    @Named("mainThread") private val androidScheduler: Scheduler

) :
    ViewModel() {


    companion object {
        val TAG = "MainViewModel"
    }


    private val data: MutableLiveData<MainState> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    private var job: Job? = null

    init {
        data.postValue(MainState())
    }

    fun getData(): LiveData<MainState> {
        return data
    }


    fun onCategoryClick(category: CATEGORY) {
        val newState = state()?.category(category)
        postState(newState)
        getMovies(newState)
        //getMovies(newState)
    }


    override fun onCleared() {
        compositeDisposable.clear()
        job?.cancel()
        super.onCleared()
    }

    fun goBack() {

        val newState = state()
            ?.loading(false)
            ?.movies(null)
            ?.category(null)
        postState(newState)
    }

    fun search(text: String?) {
        //TODO API get list does not accept a "keyword" parameter, so I cant search movies based on user's input but I will searching on local movies
        val newState = state()
            ?.keyword(text)
            ?.loading(true)

        setState(newState)
        searchMovies(newState)
    }


    /***********************Private Methods*********************************************************/


    private fun getMovies(state: MainState?) {
        val category = state?.selectedCategory
        postState(state?.loading(true))
        val sort = getSortString(category)

        job = CoroutineScope(Dispatchers.IO).launch {
            val movies = repository.getMovies(sortBy = sort)

            withContext(Dispatchers.Main) {
                if (movies != null)
                    onMoviesObtained(movies)
                else failureGettingMovies(null)

            }
        }

    }

    private fun getSortString(category: CATEGORY?): String? {
        return when (category) {
            CATEGORY.UPCOMING -> "release_date.desc"
            CATEGORY.TOP_RATED -> "vote_average.desc"
            CATEGORY.POPULAR -> "vote_average.desc" // TODO I dont find a better query for getting popular movies
            else -> null
        }
    }

    private fun onMoviesObtained(movies: List<Movie>) {
        Log.i(TAG, "Got movies")
        postState(state()?.copy(movies = movies, loading = false, error = null))
    }


    private fun failureGettingMovies(error: Throwable?) {
        var message: String? = "Unknown error"
        error?.let {
            message = apiErrorParser.parseError(error)

        }
        postState(state()?.copy(loading = false, error = message, selectedCategory = null, movies = null))

    }


    private fun searchMovies(state: MainState?) {

        //Maybe it is better  to do a  SQL QUERY rather than filtering with RX, but  if this data were taken from the webservice it would be better to do this
        val disposable = repository.getMoviesFromDb()
            .observeOn(androidScheduler)
            .subscribeOn(ioScheduler)
            .flatMap {
                Observable.fromIterable(it)
            }.filter { if (state?.keyword != null) it.title.contains(state.keyword, true) else true }
            .toList()
            .subscribe(this::onMoviesObtained) {
                state()?.loading(false)?.error("No movies with ${state?.keyword} were found")
            }

        compositeDisposable.add(disposable)
    }


    private fun state(): MainState? = data.value

    fun postState(state: MainState?) {
        data.postValue(state)
    }


    private fun setState(state: MainState?) {
        data.value = state
    }


}