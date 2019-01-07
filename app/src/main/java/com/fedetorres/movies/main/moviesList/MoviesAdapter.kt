package com.fedetorres.movies.main.moviesList

import android.content.Context
import android.content.Intent
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fedetorres.movies.GlideApp
import com.fedetorres.movies.R
import com.fedetorres.movies.main.movieDetail.MovieDetailActivity
import com.fedetorres.movies.database.entities.Movie

class MoviesAdapter(private val context: Context, var movies: MutableList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    val viewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)

        val holder = MoviesViewHolder(view)
        holder.innerRecyclerView.setRecycledViewPool(viewPool)
        return holder
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val item = movies[position]
        holder.apply {
            tvTitle.text = item.title
            tvVoteAverage.text = context.getString(R.string.vote_avg_placeholder, item.vote_average.toString())
            tvVoteCount.text = context.getString(R.string.vote_count_placeholder, item.vote_count.toString())
            tvReleaseDate.text = context.getString(R.string.release_date_placeholder, item.release_date)

            if (imageView != null)
                GlideApp.with(context)
                    .load(item.picture)
                    .placeholder(R.drawable.movie_placeholder)
                    .into(imageView)


            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra(MovieDetailActivity.MOVIE_ID, item.id)
                context.startActivity(intent)
            }


            innerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            innerRecyclerView.adapter = MovieInnerAdapter(context, item.picture)
            innerRecyclerView.addItemDecoration(ItemOffsetDecoration(40))

        }
    }


    fun updateMovies(newMovies: List<Movie>) {
        val result = DiffUtil.calculateDiff(DiffCallback(movies, newMovies))
        this.movies = newMovies.toMutableList()
        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class DiffCallback(val oldMovies: List<Movie>, val newMovies: List<Movie>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldMovies[oldItemPosition].id == newMovies[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldMovies.size
        }

        override fun getNewListSize(): Int {
            return newMovies.size
        }


        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldMovies[oldItemPosition] == newMovies[newItemPosition]
        }
    }


    inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvVoteAverage = itemView.findViewById<TextView>(R.id.tv_vote_average)
        val tvVoteCount = itemView.findViewById<TextView>(R.id.tv_vote_count)
        val imageView: ImageView? = itemView.findViewById(R.id.imageView)
        val tvReleaseDate = itemView.findViewById<TextView>(R.id.tv_release_date)
        val innerRecyclerView: OneDecoratorRecyclerView = itemView.findViewById(R.id.inner_recyclerview)
    }


}