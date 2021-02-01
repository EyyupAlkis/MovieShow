package dev.alks.movieshow.presentation.search

import dev.alks.movieshow.presentation.basemvi.BaseIntent

sealed class SearchMovieIntent : BaseIntent {
    data class SearchIntent(val searchTerm: String) : SearchMovieIntent()
    object LoadSuggestionIntent : SearchMovieIntent()
}
