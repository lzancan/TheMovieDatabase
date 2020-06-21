package com.example.themoviedatabase.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener() : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        if ((dy < 0)) {
            return
        }

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.layoutManager?.itemCount
        val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val visibleThreshold = 0

        if (totalItemCount != null) {
            if (totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                onLoadMore()
            }
        }
    }

    abstract fun onLoadMore()
}