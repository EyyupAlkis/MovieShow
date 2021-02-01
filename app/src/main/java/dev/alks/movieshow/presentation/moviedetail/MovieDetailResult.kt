package dev.alks.movieshow.presentation.moviedetail

import dev.alks.movieshow.data.datasource.model.Episode
import dev.alks.movieshow.presentation.basemvi.BaseResult
import dev.alks.movieshow.ui.search.adapter.Movie

sealed class MovieDetailResult : BaseResult {

    sealed class LoadEpisodesResult : MovieDetailResult() {
        object Loading : LoadEpisodesResult()
        data class Success(val movieList: MutableList<Episode>) : LoadEpisodesResult()
        data class Error(val throwable: Throwable) : LoadEpisodesResult()
    }

    sealed class SetMovieDetailResult : MovieDetailResult() {
        object Loading : SetMovieDetailResult()
        data class Error(val throwable: Throwable) : SetMovieDetailResult()
        data class Success(val movie: Movie) : SetMovieDetailResult()
    }
}