package com.example.themoviedatabase.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabase.R
import com.example.themoviedatabase.databinding.ItemGenreBinding
import com.example.themoviedatabase.model.Genre
import com.example.themoviedatabase.model.MoviePage

class GenresListAdapter(private val genresList: ArrayList<Genre>, private val moviePageList: ArrayList<MoviePage>): RecyclerView.Adapter<GenresListAdapter.GenreViewHolder>(), GenreClickListener {

    fun updateGenresList(newGenreList: List<Genre>) {
        genresList.clear()
        genresList.addAll(newGenreList)
        notifyDataSetChanged()
    }

    fun addMoviePageList(moviePage: MoviePage){
        val index = genresList.indexOfFirst { it.id == moviePage.genreId }
        moviePageList.add(moviePage)
        notifyItemChanged(index)
    }

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemGenreBinding>(
            inflater,
            R.layout.item_genre,
            parent,
            false
        )
        return GenreViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return genresList[position].id.toLong()
    }

    override fun getItemCount() = genresList.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genresList[position]
        holder.view.genre = genre
        holder.view.listener = this
        val moviesList = if(moviePageList.any{ it.genreId == genre.id }){moviePageList.first { it.page == 1 && it.genreId == genre.id }.results?:ArrayList()}else{ArrayList()}
        holder.view.hasMovies = moviesList.isNotEmpty()
        val childLayoutManager = LinearLayoutManager(holder.view.moviesHorizontalList.context, RecyclerView.HORIZONTAL, false)
        childLayoutManager.initialPrefetchItemCount = 4
        holder.view.moviesHorizontalList.apply {
            layoutManager = childLayoutManager
            adapter = MoviesGenreListAdapter(moviesList)
            setRecycledViewPool(viewPool)
        }
    }

    override fun onClick(v: View) {
        for (genre in genresList) {
            if (v.tag == genre.id) {
                val action = GenresFragmentDirections.actionMoviesFragment(genre)
                Navigation.findNavController(v).navigate(action)
            }
        }
    }

    class GenreViewHolder(var view: ItemGenreBinding) : RecyclerView.ViewHolder(view.root)
}