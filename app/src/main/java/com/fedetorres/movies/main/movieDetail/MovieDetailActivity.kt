package com.fedetorres.movies.main.movieDetail

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.fedetorres.movies.*
import com.fedetorres.movies.database.MovieDao
import com.fedetorres.movies.database.entities.Movie
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalArgumentException
import javax.inject.Inject

class MovieDetailActivity : BaseActivity() {

    companion object {
        val MOVIE_ID = "movieID"
    }


    @Inject
    lateinit var movieDao: MovieDao

    var movieId: Int? = null


    val imageView: ImageView by inflate(R.id.imageView)
    val tvTitle: TextView by inflate(R.id.tv_title)
    val tvDescription: TextView by inflate(R.id.tv_description)
    val tvReleaseDate: TextView by inflate(R.id.tv_release_date)
    val tvVoteCount: TextView by inflate(R.id.tv_vote_count)
    val tvVoteAverage: TextView by inflate(R.id.tv_vote_average)
    val tvPopularityValue: TextView by inflate(R.id.tv_popularity_value)
    val progressBar: ProgressBar by inflate(R.id.progressBar)

    val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)


        movieId = intent.getIntExtra(MOVIE_ID, 0)

        (application as? MoviesApplication)?.component?.inject(this)


        progressBar.visible()
        val disposable = getMovie()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { movie ->
                    progressBar.gone()
                    bindData(movie)
                },
                { error ->
                    progressBar.gone()
                    Toast.makeText(this, "Error getting movie data", Toast.LENGTH_SHORT).show()
                })


        if (disposable != null) compositeDisposable.add(disposable)
    }

    private fun getMovie(): Single<Movie> {

        return Single.create<Movie> {
            val id = movieId
            if (id != null) {
                val movie = movieDao.getMovie(id)
                it.onSuccess(movie)
            } else it.onError(IllegalArgumentException())
        }


    }

    private fun bindData(movie: Movie) {
        tvTitle.text = movie.title
        tvDescription.text = movie.overview
        tvPopularityValue.text = movie.popularity?.toString()
        tvReleaseDate.text = getString(R.string.release_date_placeholder, movie.release_date)
        tvVoteCount.text = getString(R.string.vote_count_placeholder, movie.vote_count.toString())
        tvVoteAverage.text = getString(R.string.vote_average_placeholder, movie.vote_average.toString())



        GlideApp.with(this)
            .load(movie.picture)
            .placeholder(R.drawable.movie_placeholder)
            .into(imageView)
    }

    override fun onDestroy() {

        compositeDisposable.clear()
        super.onDestroy()
    }
}
