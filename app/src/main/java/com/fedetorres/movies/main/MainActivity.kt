package com.fedetorres.movies.main

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.fedetorres.movies.BaseActivity
import com.fedetorres.movies.MoviesApplication
import com.fedetorres.movies.R
import com.fedetorres.movies.dagger.DaggerAppComponent
import javax.inject.Inject

class MainActivity : BaseActivity() {

    val tvTitle: TextView by lazy { findViewById<TextView>(R.id.tv_title) }
    val btPopular: Button by lazy { findViewById<Button>(R.id.bt_popular) }
    val btTopRated: Button by lazy { findViewById<Button>(R.id.bt_top_rated) }
    val btUpcoming: Button by lazy { findViewById<Button>(R.id.bt_upcoming) }

    var currentState: MainState? = null


    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        val app = application as? MoviesApplication
        app?.component?.inject(this)

        viewModel.getData().observe(this, Observer { if (it != null) onNewState(it) })
    }


    private fun onNewState(state: MainState) {

        if (currentState == state) return

        if (state.selectedCategory == null) {

        } else {
            //get movies
            showMoviesList() // animations
            //check if there is new movies and set items to the adapter

        }

        currentState = state

    }


    private fun showMoviesList() {

    }


    private fun bindViews() {
        btPopular.setOnClickListener { viewModel.getMovies(CATEGORY.POPULAR) }
        btTopRated.setOnClickListener { viewModel.getMovies(CATEGORY.TOP_RATED) }
        btUpcoming.setOnClickListener { viewModel.getMovies(CATEGORY.UPCOMING) }
    }
}
