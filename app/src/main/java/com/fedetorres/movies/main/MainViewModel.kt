package com.fedetorres.movies.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.fedetorres.movies.network.GetMoviesResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val moviesModel: MoviesModel) : ViewModel() {

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

        val disposable = moviesModel.getMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::onMoviesObtained,this::errorGettingMovies)

        if (disposable != null)
            compositeDisposable.add(disposable)


    }

    private fun onMoviesObtained(movies: GetMoviesResponse) {
        Log.i("ok","ok")
    }

    private fun errorGettingMovies(error: Throwable){
        Log.e("error","error!")
    }


    private fun postState(state: MainState?) {
        data.postValue(state)
    }


    private fun setState(state: MainState?) {
        data.value = state
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }


}