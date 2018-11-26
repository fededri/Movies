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
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.inputmethod.EditorInfo
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
    private val etSearch: EditText by inflate(R.id.et_search)
    private val toolbar: Toolbar by inflate(R.id.toolbar)
    private val tvNotMoviesFound: TextView by inflate(R.id.tv_no_movies_found)


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
        noMoviesFoundMessage(false)


        toolbar.animateView(
            property = View.ALPHA,
            finalViewVisibility = View.INVISIBLE,
            duration = 500,
            initialScale = 1f,
            finalScale = 0f
        )

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

        toolbar.visible()
        toolbar.animateView(
            property = View.ALPHA,
            finalViewVisibility = View.VISIBLE,
            duration = 500,
            initialScale = 0f,
            finalScale = 1f
        )

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
        btPopular.setOnClickListener { viewModel.onCategoryClick(CATEGORY.POPULAR) }
        btTopRated.setOnClickListener { viewModel.onCategoryClick(CATEGORY.TOP_RATED) }
        btUpcoming.setOnClickListener { viewModel.onCategoryClick(CATEGORY.UPCOMING) }
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


    fun onNewState(state: MainState) {

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

        if (foundMovies(state)) {
            recyclerView.gone()
            noMoviesFoundMessage(true)

        } else {
            noMoviesFoundMessage(false)
            //first hide the buttons, and when animations ends show the list of movies
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


    }


    private fun foundMovies(state: MainState?) = state?.movies != null && state.movies.isEmpty()


    private fun noMoviesFoundMessage(visible: Boolean = false) {
        if (visible) {
            tvNotMoviesFound.visible()
            tvNotMoviesFound.animateView(
                property = View.ALPHA,
                initialScale = 0f,
                finalScale = 1f,
                duration = 500,
                finalViewVisibility = View.VISIBLE
            )
        } else if (tvNotMoviesFound.visibility == View.VISIBLE) {
            //only play hide animation if this view is visible
            tvNotMoviesFound.animateView(
                property = View.ALPHA,
                initialScale = 1f,
                finalScale = 0f,
                duration = 500,
                finalViewVisibility = View.GONE
            )
        }

    }


    override fun onBackPressed() {
        if (currentState?.movies != null) viewModel.goBack() else super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
