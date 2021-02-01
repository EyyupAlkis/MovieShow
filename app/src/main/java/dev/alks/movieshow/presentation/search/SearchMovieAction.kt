package dev.alks.movieshow.presentation.search

import dev.alks.movieshow.presentation.basemvi.BaseAction

sealed class SearchMovieAction : BaseAction {
    data class SearchAction(val searchTerm: String) : SearchMovieAction()
    object LoadSuggestionAction : SearchMovieAction()
}
