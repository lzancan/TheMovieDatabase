package com.example.themoviedatabase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabase.R
import com.example.themoviedatabase.databinding.FragmentGenresBinding
import com.example.themoviedatabase.viewmodel.MoviesViewModel

class GenresFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var dataBinding: FragmentGenresBinding
    private val listAdapter = GenresListAdapter(arrayListOf(), arrayListOf()).apply { hasStableIds() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_genres, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        activity?.let { activity ->
            viewModel = ViewModelProviders.of(activity).get(MoviesViewModel::class.java)

            dataBinding.genresList.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                adapter = listAdapter
            }

            dataBinding.refreshLayout.setOnRefreshListener {
                dataBinding.genresList.visibility = View.GONE
                dataBinding.listError.visibility = View.GONE
                dataBinding.progressBar.visibility = View.VISIBLE
                viewModel.refreshBypassCache()
                dataBinding.refreshLayout.isRefreshing = false
            }

            observeViewModel()
        }
    }

    private fun observeViewModel(){
        viewModel.genres.observe(viewLifecycleOwner, Observer { list ->
            if(list.isNotEmpty()) {
                dataBinding.genresList.visibility = View.VISIBLE
                listAdapter.updateGenresList(list)
            }
        })

        viewModel.genreMoviesReceived.observe(viewLifecycleOwner, Observer {
            listAdapter.addMoviePageList(it)
        })

        viewModel.downLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let{
                dataBinding.listError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                dataBinding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    dataBinding.listError.visibility = View.GONE
                }
            }
        })
    }
}