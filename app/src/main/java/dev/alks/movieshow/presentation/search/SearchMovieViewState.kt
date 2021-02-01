package dev.alks.movieshow.presentation.search

import androidx.databinding.ObservableList
import dev.alks.movieshow.data.datasource.model.SearchItem
import dev.alks.movieshow.presentation.basemvi.BaseViewState
import io.reactivex.Observable

data class SearchMovieViewState(
    val isLoading: Boolean,
    val movieList: MutableList<SearchItem>,
    val error: Throwable?
) : BaseViewState {

    companion object {
        fun idle(): SearchMovieViewState = SearchMovieViewState(
            isLoading = false,
            movieList = mutableListOf(),
            error = null
        )
    }
}
