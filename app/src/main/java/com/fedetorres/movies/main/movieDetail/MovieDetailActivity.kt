package com.fedetorres.movies.main.movieDetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.fedetorres.movies.*
import com.fedetorres.movies.database.DbManager
import com.fedetorres.movies.network.Movie
import javax.inject.Inject

class MovieDetailActivity : BaseActivity() {

    companion object {
        val MOVIE_ID = "movieID"
    }


    @Inject
    lateinit var dbManager: DbManager

    var movieId: Int? = null
    var movie: Movie? = null

    val imageView: ImageView by inflate(R.id.imageView)
    val tvTitle: TextView by inflate(R.id.tv_title)
    val tvDescription: TextView by inflate(R.id.tv_description)
    val tvReleaseDate: TextView by inflate(R.id.tv_release_date)
    val tvVoteCount: TextView by inflate(R.id.tv_vote_count)
    val tvVoteAverage: TextView by inflate(R.id.tv_vote_average)
    val tvPopularityValue: TextView by inflate(R.id.tv_popularity_value)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)


        movieId = intent.getIntExtra(MOVIE_ID, 0)

        (application as? MoviesApplication)?.component?.inject(this)

        movie = dbManager.getMovie(movieId)
        bindData()
    }

    private fun bindData() {
        tvTitle.text = movie?.title
        tvDescription.text = movie?.overview
        tvPopularityValue.text = movie?.popularity?.toString()
        tvReleaseDate.text = getString(R.string.release_date_placeholder, movie?.release_date)
        tvVoteCount.text = getString(R.string.vote_count_placeholder, movie?.vote_count?.toString())
        tvVoteAverage.text = getString(R.string.vote_average_placeholder, movie?.vote_average?.toString())



        GlideApp.with(this)
            .load(movie?.picture)
            .placeholder(R.drawable.movie_placeholder)
            .into(imageView)
    }
}
