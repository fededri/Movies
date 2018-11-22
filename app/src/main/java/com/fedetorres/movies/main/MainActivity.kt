package com.fedetorres.movies.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.*
import com.fedetorres.movies.*
import com.fedetorres.movies.main.moviesList.ItemOffsetDecoration
import com.fedetorres.movies.main.moviesList.MoviesAdapter
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private val tvTitle: TextView by inflate(R.id.tv_title)
    private val btPopular: Button by inflate(R.id.bt_popular)
    private val btTopRated: Button by inflate(R.id.bt_top_rated)
    private val btUpcoming: Button by inflate(R.id.bt_upcoming)
    private val progressBar: ProgressBar  by inflate(R.id.progressBar)
    private val recyclerView: RecyclerView by inflate(R.id.recyclerView)

    val buttons = mutableListOf<Button>()

    var currentState: MainState? = null


    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        //showButtons()

        val app = application as? MoviesApplication
        app?.component?.inject(this)


        viewModel.getData().observe(this, Observer { if (it != null) onNewState(it) })
    }

    private fun showButtons() {
        recyclerView.gone()
        tvTitle.visible()
        buttons.forEach {
            it.visible()

            val scaleX = ObjectAnimator.ofFloat(it, View.SCALE_X, 0f, 1f)
            val scaleY = ObjectAnimator.ofFloat(it, View.SCALE_Y, 0f, 1f)
            val alpha = ObjectAnimator.ofFloat(it, View.ALPHA, 0.8f, 1f)
            it.playAnimations(listOf(scaleX, scaleY, alpha), 1500, BounceInterpolator())
        }
    }

    private fun hideButtons(onHideEnd: (() -> Unit)? = null) {
        tvTitle.gone()
        var counter = 1
        val listener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                counter++
                if (counter == buttons.size) onHideEnd?.invoke()
            }
        }

        buttons.forEach {
            it.animateView(
                property = View.ALPHA,
                finalViewVisibility = View.GONE,
                duration = 500,
                initialScale = 1f,
                finalScale = 0f,
                listener = listener
            )
        }
    }

    private fun bindViews() {
        btPopular.setOnClickListener { viewModel.getMovies(CATEGORY.POPULAR) }
        btTopRated.setOnClickListener { viewModel.getMovies(CATEGORY.TOP_RATED) }
        btUpcoming.setOnClickListener { viewModel.getMovies(CATEGORY.UPCOMING) }
        buttons.add(btPopular)
        buttons.add(btTopRated)
        buttons.add(btUpcoming)
    }


    private fun onNewState(state: MainState) {

        if (currentState == state) return


        updateProgressBar(state)
        checkError(state)
        if (state.selectedCategory == null) {
            showButtons()
        } else {
            showMoviesList(state) // animations

        }

        currentState = state

    }


    private fun updateProgressBar(state: MainState) {
        when {
            state.loading -> progressBar.visible()
            else -> progressBar.gone()
        }

    }

    private fun checkError(state: MainState) {
        if (state.error != null) {
            Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
        }
    }


    private fun showMoviesList(state: MainState) {
        hideButtons {
            recyclerView.visible()
            if (state.movies != null) {
                if (recyclerView.adapter == null) {
                    val adapter = MoviesAdapter(this, state.movies.toMutableList())
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                    recyclerView.addItemDecoration(ItemOffsetDecoration(this, R.dimen.movie_card_offset))

                } else {
                    val adapter = recyclerView.adapter as? MoviesAdapter
                    adapter?.movies?.clear()
                    adapter?.movies?.addAll(state.movies)
                    adapter?.notifyDataSetChanged()
                }
            }
        }


    }


    override fun onBackPressed() {
        if (currentState?.movies != null) viewModel.goBack() else super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
