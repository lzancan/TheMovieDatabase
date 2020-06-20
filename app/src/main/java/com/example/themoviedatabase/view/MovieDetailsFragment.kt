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
import com.example.themoviedatabase.databinding.FragmentDetailsBinding
import com.example.themoviedatabase.model.Movie
import com.example.themoviedatabase.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class MovieDetailsFragment : Fragment() {

    var movie: Movie? = null

    private lateinit var viewModel: MoviesViewModel
    private lateinit var dataBinding: FragmentDetailsBinding

    private var listAdapter: MoviesListAdapter = MoviesListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            movie = MovieDetailsFragmentArgs.fromBundle(it).movie
        }

        activity?.let { activity ->
            movie?.let { movie ->
                viewModel = ViewModelProviders.of(activity).get(MoviesViewModel::class.java)
                viewModel.toolbarName.value = movie.movieTitle
                dataBinding.lifecycleOwner = this

                dataBinding.movie = movie

                similarList.apply {
                    layoutManager = GridLayoutManager(context, 3)
                    adapter = listAdapter
                }

                viewModel.similarMovies.observe(viewLifecycleOwner, Observer {
                    if(it.isEmpty()){
                        similarList.visibility = View.GONE
                        textSimilar.visibility = View.GONE
                    } else {
                        listAdapter.updateMoviesList(it)
                    }
                })

                viewModel.getSimilarMovies(movie.movieId)
            }
        }
    }
}