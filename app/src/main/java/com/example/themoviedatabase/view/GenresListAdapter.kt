package com.example.themoviedatabase.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabase.R
import com.example.themoviedatabase.databinding.ItemGenreBinding
import com.example.themoviedatabase.model.Genre

class GenresListAdapter(private val genresList: ArrayList<Genre>): RecyclerView.Adapter<GenresListAdapter.GenreViewHolder>(), GenreClickListener {

    fun updateGenresList(newGenreList: List<Genre>) {
        genresList.clear()
        genresList.addAll(newGenreList)
        notifyDataSetChanged()
    }

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

    override fun getItemCount() = genresList.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.view.genre = genresList[position]
        holder.view.listener = this
    }

    override fun onClick(v: View) {
        for (genre in genresList) {
            if (v.tag == genre.name) {
                val action = GenresFragmentDirections.actionMoviesFragment(genre)
                Navigation.findNavController(v).navigate(action)
            }
        }
    }

    class GenreViewHolder(var view: ItemGenreBinding) : RecyclerView.ViewHolder(view.root)
}