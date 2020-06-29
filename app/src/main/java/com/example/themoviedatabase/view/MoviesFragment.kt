package com.example.themoviedatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.themoviedatabase.R
import com.example.themoviedatabase.databinding.FragmentMoviesBinding
import com.example.themoviedatabase.model.Genre
import com.example.themoviedatabase.viewmodel.MoviesViewModel

class MoviesFragment : Fragment() {

    var genre: Genre? = null

    private lateinit var viewModel: MoviesViewModel
    private lateinit var dataBinding: FragmentMoviesBinding
    private var listAdapter: MoviesListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            genre = MoviesFragmentArgs.fromBundle(it).genre
        }

        activity?.let { activity ->
            viewModel = ViewModelProviders.of(activity).get(MoviesViewModel::class.java)

            viewModel.toolbarName.value = genre?.name

            genre?.let { genre ->
                viewModel.moviePages.value?.let { moviePages ->
                    listAdapter = MoviesListAdapter(
                        moviePages[genre.id]?.firstOrNull { it.genreId == genre.id }?.results
                            ?: ArrayList()
                    )

                    dataBinding.moviesList.apply {
                        layoutManager = GridLayoutManager(context, 3)
                        adapter = listAdapter
                    }
                    dataBinding.moviesList.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
                        override fun onLoadMore() {
                            if (!viewModel.isRequestingMovies) {
                                viewModel.isRequestingMovies = true
                                viewModel.getMoviesFromGenreFromApi(
                                    genre.id,
                                    (viewModel.moviePages.value!![genre.id]?.maxBy { it.page }?.page
                                        ?: 1) + 1,
                                    false
                                )
                            }
                        }
                    })
                }

                observeViewModel()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.isRequestingMovies = false
    }

    private fun observeViewModel() {
        viewModel.genreMoviesReceived.observe(viewLifecycleOwner, Observer {
            listAdapter?.appendPageToMoviesList(it.results ?: ArrayList())
            viewModel.isRequestingMovies = false
        })
    }
}