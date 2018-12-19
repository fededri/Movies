package com.fedetorres.movies.main.moviesList

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fedetorres.movies.GlideApp
import com.fedetorres.movies.R

class MovieInnerAdapter(val context: Context, val image: String) :
    RecyclerView.Adapter<MovieInnerAdapter.InnerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.inner_item, parent, false)
        return InnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {

        if (holder.imageView != null) {
            GlideApp.with(context)
                .load(image)
                .placeholder(R.drawable.movie_placeholder)
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }




    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView? = itemView.findViewById(R.id.imageView)
    }
}