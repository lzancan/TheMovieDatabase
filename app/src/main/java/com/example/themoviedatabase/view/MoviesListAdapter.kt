package com.example.themoviedatabase.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabase.R
import com.example.themoviedatabase.databinding.ItemMovieBinding
import com.example.themoviedatabase.model.Movie
import com.example.themoviedatabase.model.MoviePage

class MoviesListAdapter(private val moviesList: ArrayList<Movie>): RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder>(), MovieClickListener {

    fun updateMoviesList(newMoviesList: List<Movie>) {
        moviesList.clear()
        moviesList.addAll(newMoviesList)
        notifyDataSetChanged()
    }

    fun appendPageToMoviesList(moviesListToAdd: List<Movie>) {
        moviesList.addAll(moviesListToAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemMovieBinding>(
            inflater,
            R.layout.item_movie,
            parent,
            false
        )
        return MovieViewHolder(view)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.movie = moviesList[position]
        holder.view.listener = this
    }

    override fun onClick(v: View) {
        for (movie in moviesList) {
            if (v.tag == movie.movieId) {
                val action = if(Navigation.findNavController(v).currentDestination?.id == R.id.movieDetailsFragment){
                    MovieDetailsFragmentDirections.actionAnotherMovieDetailsFragment(movie)
                } else {
                    MoviesFragmentDirections.actionMovieDetailsFragment(movie)
                }
                Navigation.findNavController(v).navigate(action)
            }
        }
    }

    class MovieViewHolder(var view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root)
}