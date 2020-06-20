package com.example.themoviedatabase.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabase.R
import com.example.themoviedatabase.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_genres.*

class GenresFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private val listAdapter = GenresListAdapter(arrayListOf(), arrayListOf()).apply { hasStableIds() }

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

        viewModel.refresh()

        genresList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = listAdapter
        }

        refreshLayout.setOnRefreshListener {
            genresList.visibility = View.GONE
            listError.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.genres.observe(viewLifecycleOwner, Observer { list ->
            listAdapter.updateGenresList(list)
        })

        viewModel.genreMoviesReceived.observe(viewLifecycleOwner, Observer {
            Log.d("exc19062020", "genre movies received - $it")
            listAdapter.addMoviePageList(it)
        })

        viewModel.downLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let{
                listError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                progressBar.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    listError.visibility = View.GONE
                    genresList.visibility = View.VISIBLE
                }
            }
        })
    }
}