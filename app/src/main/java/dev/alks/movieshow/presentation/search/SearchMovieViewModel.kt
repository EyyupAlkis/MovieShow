package dev.alks.movieshow.presentation.search

import androidx.lifecycle.ViewModel
import dev.alks.movieshow.presentation.basemvi.BaseViewModel
import dev.alks.movieshow.presentation.search.SearchMovieResult.SearchResult
import dev.alks.movieshow.ui.search.adapter.MovieSearchAdapter
import dev.alks.movieshow.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SearchMovieViewModel @Inject constructor(
    private val actionProcessorHolder: SearchMovieActionProcessorHolder
) : ViewModel(), BaseViewModel<SearchMovieIntent, SearchMovieViewState> {


    private val intentSubject: PublishSubject<SearchMovieIntent> = PublishSubject.create()
    private val statesObservable: Observable<SearchMovieViewState> = compose()

    override fun processIntents(intents: Observable<SearchMovieIntent>) {
        intents.subscribe(intentSubject)
    }

    override fun states(): Observable<SearchMovieViewState> = statesObservable

    private val intentFilter: ObservableTransformer<SearchMovieIntent, SearchMovieIntent>
        get() = ObservableTransformer { intents ->
            intents.publish() { shared ->
                Observable.merge(
                    shared.ofType(SearchMovieIntent.LoadSuggestionIntent::class.java),
                    shared.notOfType(SearchMovieIntent.LoadSuggestionIntent::class.java)
                )
            }
        }

    fun compose(): Observable<SearchMovieViewState> {
        return intentSubject
            .compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessorHolder.actionProcessor)
            .scan(SearchMovieViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    fun actionFromIntent(intent: SearchMovieIntent): SearchMovieAction {
        return when (intent) {
            is SearchMovieIntent.SearchIntent -> SearchMovieAction.SearchAction(intent.searchTerm)
            is SearchMovieIntent.LoadSuggestionIntent -> SearchMovieAction.LoadSuggestionAction
        }
    }

    companion object {

        private val reducer =
            BiFunction { previousState: SearchMovieViewState, result: SearchMovieResult ->
                when (result) {
                    is SearchResult -> when (result) {
                        is SearchResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                movieList = result.searchResponse,
                                error = null
                            )
                        }
                        is SearchResult.Error -> {
                            previousState.copy(isLoading = false, error = result.throwable)
                        }
                        is SearchResult.Loading -> {
                            previousState.copy(isLoading = false, error = null)
                        }
                    }
                    is SearchMovieResult.LoadSuggestionResult -> when (result) {
                        is SearchMovieResult.LoadSuggestionResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                movieList = result.suggestionList,
                                error = null
                            )
                        }
                        is SearchMovieResult.LoadSuggestionResult.Error -> {
                            previousState.copy(isLoading = false, error = result.throwable)
                        }
                        is SearchMovieResult.LoadSuggestionResult.Loading -> {
                            previousState.copy(isLoading = false, error = null)
                        }
                    }
                }
            }
    }

}