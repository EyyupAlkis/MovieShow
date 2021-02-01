package dev.alks.movieshow.presentation.moviedetail

import androidx.lifecycle.ViewModel
import dev.alks.movieshow.presentation.basemvi.BaseViewModel
import dev.alks.movieshow.presentation.moviedetail.MovieDetailAction.LoadEpisodesAction
import dev.alks.movieshow.presentation.moviedetail.MovieDetailIntent.LoadEpisodesIntent
import dev.alks.movieshow.presentation.moviedetail.MovieDetailResult.LoadEpisodesResult
import dev.alks.movieshow.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val actionProcessorHolder: MovieDetailActionProcessHolder
) : ViewModel(), BaseViewModel<MovieDetailIntent, MovieDetailViewState> {

    private val intentSubject: PublishSubject<MovieDetailIntent> = PublishSubject.create()
    private val statesObservable: Observable<MovieDetailViewState> = compose()


    override fun processIntents(intents: Observable<MovieDetailIntent>) {
        intents.subscribe(intentSubject)
    }

    override fun states(): Observable<MovieDetailViewState> = statesObservable

    private val intentFilterForMovieDetail: ObservableTransformer<MovieDetailIntent, MovieDetailIntent>
        get() = ObservableTransformer { intents ->
            intents.publish() { shared ->
                Observable.merge(
                    shared.ofType(MovieDetailIntent.SetMovieDetailIntent::class.java),
                    shared.notOfType(MovieDetailIntent.SetMovieDetailIntent::class.java)
                )
            }
        }


    private val intentFilterForEpisodes: ObservableTransformer<MovieDetailIntent, MovieDetailIntent>
        get() = ObservableTransformer { intents ->
            intents.publish() { shared ->
                Observable.merge(
                    shared.ofType(LoadEpisodesIntent::class.java),
                    shared.notOfType(LoadEpisodesIntent::class.java)
                )
            }
        }


    fun compose(): Observable<MovieDetailViewState> {
        return intentSubject
            .compose(intentFilterForMovieDetail)
            .compose(intentFilterForEpisodes)
            .map(this::actionFromIntent)
            .compose(actionProcessorHolder.actionProcessor)
            .scan(MovieDetailViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: MovieDetailIntent): MovieDetailAction {
        return when (intent) {
            is LoadEpisodesIntent -> LoadEpisodesAction(intent.movieId)
            is MovieDetailIntent.SetMovieDetailIntent -> MovieDetailAction.SetMovieDetailAction(intent.movie)
        }
    }

    companion object {
        private val reducer =
            BiFunction { previousState: MovieDetailViewState, result: MovieDetailResult ->
                when (result) {
                    is LoadEpisodesResult -> when (result) {
                        is LoadEpisodesResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                episodeList = result.movieList,
                                error = null
                            )
                        }
                        is LoadEpisodesResult.Error -> {
                            previousState.copy(isLoading = false, error = result.throwable)
                        }
                        is LoadEpisodesResult.Loading -> {
                            previousState.copy(isLoading = true, error = null)
                        }
                    }
                    is MovieDetailResult.SetMovieDetailResult -> when (result) {
                        is MovieDetailResult.SetMovieDetailResult.Success -> {
                            previousState.copy(
                                isLoading = false,
                                movie = result.movie,
                                error = null
                            )
                        }
                        is MovieDetailResult.SetMovieDetailResult.Loading -> {
                            previousState.copy(isLoading = true, error = null)

                        }
                        is MovieDetailResult.SetMovieDetailResult.Error -> {
                            previousState.copy(isLoading = false, error = result.throwable)
                        }
                    }
                }
            }
    }
}