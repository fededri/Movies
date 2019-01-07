package com.fedetorres.movies.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
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
    private var moviesAreShown = false
    var searchKeyword = ObservableField<String>()
    private var category: CATEGORY = CATEGORY.POPULAR


    init {
        searchKeyword.set("")
        postState(MainState.ShowButtons)
    }


    fun getData(): LiveData<MainState> {
        return data
    }


    fun onCategoryClick(category: CATEGORY) {

        this.category = category
        getMovies()
        //getMovies(newState)
    }


    override fun onCleared() {
        compositeDisposable.clear()
        job?.cancel()
        super.onCleared()
    }

    fun goBack() {
        if (moviesAreShown) {
            moviesAreShown = false
            postState(MainState.ShowButtons)
        } else {
            postState(MainState.GoBack)
        }

    }

    fun search(text: String?) {
        //TODO API get list does not accept a "keyword" parameter, so I cant search movies based on user's input but I will searching on local movies
        text?.apply {
            searchKeyword.set(this)
        }
        setState(MainState.Loading(false))
        searchMovies()
    }


    /***********************Private Methods*********************************************************/


    private fun getMovies() {

        postState(MainState.Loading(true))
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
        postState(MainState.Movies(movies))
        moviesAreShown = true
    }


    private fun failureGettingMovies(error: Throwable?) {
        var message: String? = "Unknown error"
        error?.let {
            message = apiErrorParser.parseError(error)

        }
        message?.apply {
            postState(MainState.Error(this))
        }

    }


    private fun searchMovies() {

        //Maybe it is better  to do a  SQL QUERY rather than filtering with RX, but  if this data were taken from the webservice it would be better to do this
        val disposable = repository.getMoviesFromDb()
            .observeOn(androidScheduler)
            .subscribeOn(ioScheduler)
            .flatMap {
                Observable.fromIterable(it)
            }.filter {
                if (searchKeyword.get()?.isNotBlank() == true) it.title.contains(
                    searchKeyword.get()!!,
                    true
                ) else true
            }
            .toList()
            .subscribe(this::onMoviesObtained) {
                postState(MainState.NoMoviesFoundOnSearch)
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