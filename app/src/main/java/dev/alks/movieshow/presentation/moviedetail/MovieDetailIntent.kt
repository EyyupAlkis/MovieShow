package dev.alks.movieshow.presentation.moviedetail

import dev.alks.movieshow.presentation.basemvi.BaseIntent
import dev.alks.movieshow.ui.search.adapter.Movie

sealed class MovieDetailIntent : BaseIntent {
    data class LoadEpisodesIntent(val movieId:Int) : MovieDetailIntent()
    data class SetMovieDetailIntent(val movie: Movie): MovieDetailIntent()
}