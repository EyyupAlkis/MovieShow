package dev.alks.movieshow.presentation.search

import androidx.databinding.ObservableList
import dev.alks.movieshow.data.datasource.model.SearchItem
import dev.alks.movieshow.presentation.basemvi.BaseResult
import io.reactivex.Observable

sealed class SearchMovieResult : BaseResult {

    sealed class SearchResult : SearchMovieResult() {
        data class Success(val searchResponse: MutableList<SearchItem>) : SearchResult()
        data class Error(val throwable: Throwable) : SearchResult()
        object Loading : SearchResult()
    }

    sealed class LoadSuggestionResult : SearchMovieResult() {
        data class Success(val suggestionList: MutableList<SearchItem>) : LoadSuggestionResult()
        data class Error(val throwable: Throwable) : LoadSuggestionResult()
        object Loading : LoadSuggestionResult()
    }
}
