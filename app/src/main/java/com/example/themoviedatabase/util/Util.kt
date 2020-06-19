package com.example.themoviedatabase.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themoviedatabase.R

object Util {

    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"

    private fun ImageView.loadMovieImage(uri: String?, progressDrawable: CircularProgressDrawable){
        val options = RequestOptions()
            .placeholder(progressDrawable)
            .error(R.mipmap.ic_launcher)
        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(uri)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("android:imageUrl")
    fun loadMovieImage(view: ImageView, url: String?){
        url?.let {
            view.loadMovieImage("$IMAGE_BASE_URL$url", getProgressDrawable(view.context))
        }
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun View.setVisibility(visible: Boolean?){
        visible?.let {
            visibility = if(it){View.VISIBLE}else{View.GONE}
        }
    }

    private fun getProgressDrawable(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 10f
            centerRadius = 50f
            start()
        }
    }
}