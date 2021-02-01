package dev.alks.movieshow.presentation.moviedetail

import androidx.databinding.ObservableList
import dev.alks.movieshow.data.datasource.model.Episode
import dev.alks.movieshow.presentation.basemvi.BaseViewState
import dev.alks.movieshow.ui.search.adapter.Movie
import io.reactivex.Observable

data class MovieDetailViewState(
    val isLoading: Boolean,
    var episodeList: MutableList<Episode>,
    val movie: Movie?,
    val error: Throwable?
) : BaseViewState {
    companion object {
        fun idle(): MovieDetailViewState = MovieDetailViewState(
            isLoading = false,
            movie = null,
            episodeList = mutableListOf(),
            error = null
        )
    }
}