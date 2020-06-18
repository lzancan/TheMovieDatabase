package com.example.themoviedatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.themoviedatabase.R
import com.example.themoviedatabase.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_genres.*

class GenresFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private val listAdapter = GenresListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_genres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)

        viewModel.genres.observe(viewLifecycleOwner, Observer { list ->
            listAdapter.updateGenresList(list)
        })

        genresList.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = listAdapter
        }

        viewModel.getGenres()
        //viewModel.getMoviesFromGenre(80, 2)

    }
}