package dev.alks.movieshow.presentation.search

import dev.alks.movieshow.data.domain.SearchUseCase
import dev.alks.movieshow.presentation.search.SearchMovieAction.SearchAction
import dev.alks.movieshow.presentation.search.SearchMovieResult.SearchResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchMovieActionProcessorHolder @Inject constructor(
    private val searchUseCase: SearchUseCase
) {

    private val movieSearchProcessor =
        ObservableTransformer<SearchAction, SearchResult> { action ->
            action.flatMap {
                searchUseCase.invoke(it.searchTerm)
                    .map { movies -> SearchResult.Success(movies.toMutableList()) }
                    .cast(SearchResult::class.java)
                    .onErrorReturn(SearchResult::Error)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(SearchResult.Loading)
            }
        }

    private val loadingSuggestionProcessor =
        ObservableTransformer<SearchMovieAction.LoadSuggestionAction, SearchMovieResult.LoadSuggestionResult> { action ->
            action.flatMap {
                searchUseCase.invoke("friend")
                    .map { movies -> SearchMovieResult.LoadSuggestionResult.Success(movies.toMutableList()) }
                    .cast(SearchMovieResult.LoadSuggestionResult::class.java)
                    .onErrorReturn(SearchMovieResult.LoadSuggestionResult::Error)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(SearchMovieResult.LoadSuggestionResult.Loading)
            }
        }


    internal var actionProcessor =
        ObservableTransformer<SearchMovieAction, SearchMovieResult> { action ->
            action.publish { shared ->
                Observable.merge(
                    shared.ofType(SearchAction::class.java).compose(movieSearchProcessor),
                    shared.ofType(SearchMovieAction.LoadSuggestionAction::class.java)
                        .compose(loadingSuggestionProcessor)
                ).mergeWith(
                        shared.filter {
                            it !is SearchAction && it !is SearchMovieAction.LoadSuggestionAction
                        }.flatMap { error ->
                            Observable.error<SearchMovieResult>(
                                IllegalStateException("Unknown Action type : $error")
                            )
                        }
                    )
            }
        }

}