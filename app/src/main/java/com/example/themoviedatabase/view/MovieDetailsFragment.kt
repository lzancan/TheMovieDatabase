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
import com.example.themoviedatabase.util.Util.animateOnShow
import com.example.themoviedatabase.viewmodel.MoviesViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


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

                dataBinding.similarList.apply {
                    layoutManager = GridLayoutManager(context, 3)
                    adapter = listAdapter
                }

                viewModel.getVideosFromMovie(movie.movieId)
                viewModel.getSimilarMovies(movie.movieId)

                lifecycle.addObserver(dataBinding.youtubePlayerView)

                observeViewModel()
            }
        }
    }

    private fun observeViewModel(){
        viewModel.similarMovies.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                dataBinding.similarList.visibility = View.GONE
                dataBinding.textSimilar.visibility = View.GONE
            } else {
                listAdapter.updateMoviesList(it)
                dataBinding.similarList.visibility = View.VISIBLE
                dataBinding.textSimilar.visibility = View.VISIBLE
            }
        })
        viewModel.videosFromMovie.observe(viewLifecycleOwner, Observer { movieVideosList ->
            if (movieVideosList.isEmpty()) {
                dataBinding.youtubePlayerView.visibility = View.GONE
            } else {
                dataBinding.youtubePlayerView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        movieVideosList.firstOrNull{it.videoSite == "YouTube"}?.videoKey?.let { videoId ->
                            dataBinding.youtubePlayerView.animateOnShow()
                            youTubePlayer.cueVideo(videoId, 0f)
                        }
                    }
                })
            }
        })
    }
}