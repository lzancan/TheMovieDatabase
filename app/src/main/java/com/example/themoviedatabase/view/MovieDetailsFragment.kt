package com.example.themoviedatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.themoviedatabase.R
import com.example.themoviedatabase.databinding.FragmentDetailsBinding
import com.example.themoviedatabase.model.Movie
import com.example.themoviedatabase.viewmodel.MoviesViewModel

class MovieDetailsFragment : Fragment() {

    var movie: Movie? = null

    private lateinit var viewModel: MoviesViewModel
    private lateinit var dataBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            movie = MovieDetailsFragmentArgs.fromBundle(it).movie
        }

        setHasOptionsMenu(true)

        activity?.let { activity ->
            viewModel = ViewModelProviders.of(activity).get(MoviesViewModel::class.java)
            dataBinding.lifecycleOwner = this

            dataBinding.movie = movie
        }

    }
}