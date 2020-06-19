package com.example.themoviedatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.themoviedatabase.R
import com.example.themoviedatabase.model.Genre
import com.example.themoviedatabase.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : Fragment() {

    var genre: Genre? = null

    private lateinit var viewModel: MoviesViewModel
    private var listAdapter: MoviesListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            genre = MoviesFragmentArgs.fromBundle(it).genre
        }
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)

        listAdapter = MoviesListAdapter(genre?.moviePages?.firstOrNull()?.results?:ArrayList())

        moviesList.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = listAdapter
        }

        genre?.let {
            viewModel.getMoviesFromGenre(it.id, 1)
        }
    }
}