package com.example.themoviedatabase.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabase.R
import com.example.themoviedatabase.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_genres.*

class GenresFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private val listAdapter = GenresListAdapter(arrayListOf()).apply { hasStableIds() }

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

        viewModel.genreMoviesReceived.observe(viewLifecycleOwner, Observer {
            Log.d("tag", "genre movies received - $it")
            listAdapter.updateGenreMoviesList(it)
        })

        genresList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = listAdapter
        }

        viewModel.getGenres()
        //viewModel.getMoviesFromGenre(80, 2)

    }
}