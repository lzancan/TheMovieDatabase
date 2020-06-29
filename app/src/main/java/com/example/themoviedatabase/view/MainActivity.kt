package com.example.themoviedatabase.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.themoviedatabase.R
import com.example.themoviedatabase.databinding.ActivityMainBinding
import com.example.themoviedatabase.viewmodel.MoviesViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var viewModel: MoviesViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)

        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        viewModel.toolbarName.observe(this, Observer {
            binding.toolbar.title = it
        })

        viewModel.refreshGenres()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }


}