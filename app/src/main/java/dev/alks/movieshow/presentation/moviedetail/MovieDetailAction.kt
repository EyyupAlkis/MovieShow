package dev.alks.movieshow.presentation.moviedetail

import dev.alks.movieshow.ui.search.adapter.Movie

sealed class MovieDetailAction {
    data class LoadEpisodesAction(val movieId: Int) : MovieDetailAction()
    data class SetMovieDetailAction(val movie: Movie) : MovieDetailAction()
}
