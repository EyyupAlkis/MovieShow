package dev.alks.movieshow.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.alks.movieshow.data.datasource.model.SearchItem
import dev.alks.movieshow.data.datasource.model.Show
import dev.alks.movieshow.databinding.MovieListSingleItemBinding
import dev.alks.movieshow.utils.setHtmlText
import dev.alks.movieshow.utils.setImageUrl
import dev.alks.movieshow.utils.setRate

class MovieSearchAdapter(private val movieList: MutableList<SearchItem>) :
    RecyclerView.Adapter<MovieSearchAdapter.MyViewHolder>() {

    interface MovieListener {
        fun onMovieClick(movie: Movie)
    }

    lateinit var listener: MovieListener


    fun updateMovieList(movies: MutableList<SearchItem>) {
        this.movieList.clear()
        this.movieList.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            MovieListSingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(movieList[position].show)
    }

    override fun getItemCount(): Int = movieList.size

    inner class MyViewHolder(val binding: MovieListSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(show: Show) {
            val movie = Movie(movie = show)

            movie.smallImageUrl.get()?.let { binding.imgMovie.setImageUrl(it) }
            binding.txtMovieRate.setRate(movie.rating.get())
            binding.txtMovieName.text = movie.movieName.get()
            movie.definition.get()?.let { binding.txtMovieDesc.setHtmlText(it) }


            binding.root.setOnClickListener {
                listener.onMovieClick(movie = movie)
            }

        }
    }
}