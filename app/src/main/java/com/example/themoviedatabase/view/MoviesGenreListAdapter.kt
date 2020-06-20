package com.example.themoviedatabase.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabase.R
import com.example.themoviedatabase.databinding.ItemMovieGenreBinding
import com.example.themoviedatabase.model.Movie

class MoviesGenreListAdapter(private val moviesList: ArrayList<Movie>): RecyclerView.Adapter<MoviesGenreListAdapter.MovieGenreViewHolder>(), MovieClickListener {

    fun updateMoviesList(moviesList: List<Movie>) {
        this.moviesList.clear()
        this.moviesList.addAll(moviesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemMovieGenreBinding>(
            inflater,
            R.layout.item_movie_genre,
            parent,
            false
        )
        return MovieGenreViewHolder(view)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MovieGenreViewHolder, position: Int) {
        holder.view.movie = moviesList[position]
        holder.view.listener = this
    }

    override fun onClick(v: View) {
        for (movie in moviesList) {
            if (v.tag == movie.movieId) {
                val action = GenresFragmentDirections.actionMovieDetailsFragment(movie)
                Navigation.findNavController(v).navigate(action)
            }
        }
    }

    class MovieGenreViewHolder(var view: ItemMovieGenreBinding) : RecyclerView.ViewHolder(view.root)
}