package dev.alks.movieshow.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.alks.movieshow.presentation.search.SearchMovieViewModel

class ViewModelFactory private constructor(private val context:Context): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == SearchMovieViewModel::class.java){

        }
        throw IllegalArgumentException("Unknown ViewModel class $modelClass")
    }
}