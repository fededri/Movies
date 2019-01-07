package com.fedetorres.movies.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.fedetorres.movies.*
import com.fedetorres.movies.databinding.ActivityMainBinding
import com.fedetorres.movies.main.moviesList.ItemOffsetDecoration
import com.fedetorres.movies.main.moviesList.MoviesAdapter
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity() {

    val buttons = mutableListOf<Button>()


    private var binding: ActivityMainBinding? = null


    @Inject
    lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.mainViewModel = viewModel

        bindViews()


        viewModel.getData().observe(this, Observer { if (it != null) handleChanges(it) })
    }

    private fun showButtons() {
        noMoviesFoundMessage(false)
        binding?.apply {
            recyclerView.gone()
            tvTitle.visible()
            toolbar.animateView(
                property = View.ALPHA,
                finalViewVisibility = View.INVISIBLE,
                duration = 500,
                initialScale = 1f,
                finalScale = 0f
            )
        }


        buttons.forEach {
            it.visible()

            val scaleX = ObjectAnimator.ofFloat(it, View.SCALE_X, 0f, 1f)
            val scaleY = ObjectAnimator.ofFloat(it, View.SCALE_Y, 0f, 1f)
            val alpha = ObjectAnimator.ofFloat(it, View.ALPHA, 0.8f, 1f)
            it.playAnimations(listOf(scaleX, scaleY, alpha), 1500, BounceInterpolator())
        }
    }


    private fun hideButtons(onHideEnd: (() -> Unit)? = null) {

        var counter = 1
        val listener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                counter++
                if (counter == buttons.size) onHideEnd?.invoke()
            }
        }

        binding?.apply {
            tvTitle.gone()
            toolbar.visible()
            toolbar.animateView(
                property = View.ALPHA,
                finalViewVisibility = View.VISIBLE,
                duration = 500,
                initialScale = 0f,
                finalScale = 1f
            )
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
        binding?.apply {
            buttons.add(btPopular)
            buttons.add(btTopRated)
            buttons.add(btUpcoming)

            etSearch.setOnEditorActionListener { v, actionId, event ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        viewModel.search(etSearch.text.toString())
                        true
                    }
                    else -> false

                }
            }
        }
    }


    fun handleChanges(state: MainState) {

        when (state) {
            MainState.GoBack -> super.onBackPressed()
            MainState.ShowButtons -> showButtons()
            is MainState.Movies -> showMoviesList(state)
            is MainState.Loading -> updateProgressBar(state.loading)
            is MainState.Error -> showError(state.message)

        }

    }


    private fun updateProgressBar(loading: Boolean) {
        when {
            loading -> binding?.progressBar?.visible()
            else -> binding?.progressBar?.gone()
        }

    }

    private fun showError(error: String) {

        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()

    }


    private fun showMoviesList(state: MainState.Movies) {
        updateProgressBar(false)
        if (state.movies.isEmpty()) {
            binding?.recyclerView?.gone()
            noMoviesFoundMessage(true)

        } else {
            noMoviesFoundMessage(false)

            //first hide the buttons, and when animations ends show the list of movies
            hideButtons {
                binding?.recyclerView?.visible()
                if (state.movies != null) {
                    if (binding?.recyclerView?.adapter == null) {
                        val adapter = MoviesAdapter(this, state.movies.toMutableList())
                        binding?.recyclerView?.adapter = adapter
                        binding?.recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        binding?.recyclerView?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                        binding?.recyclerView?.addItemDecoration(ItemOffsetDecoration(this, R.dimen.movie_card_offset))

                    } else {
                        val adapter = binding?.recyclerView?.adapter as? MoviesAdapter
                        adapter?.updateMovies(state.movies)
                    }
                }
            }
        }


    }


    private fun noMoviesFoundMessage(visible: Boolean = false) {
        if (visible) {
            binding?.tvNoMoviesFound?.visible()
            binding?.tvNoMoviesFound?.animateView(
                property = View.ALPHA,
                initialScale = 0f,
                finalScale = 1f,
                duration = 500,
                finalViewVisibility = View.VISIBLE
            )
        } else if (binding?.tvNoMoviesFound?.visibility == View.VISIBLE) {
            //only play hide animation if this view is visible
            binding?.tvNoMoviesFound?.animateView(
                property = View.ALPHA,
                initialScale = 1f,
                finalScale = 0f,
                duration = 500,
                finalViewVisibility = View.GONE
            )
        }

    }


    override fun onBackPressed() {
        viewModel.goBack()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
