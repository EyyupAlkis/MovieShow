package dev.alks.movieshow.presentation.moviedetail

import dev.alks.movieshow.data.domain.MovieDetailUseCase
import dev.alks.movieshow.presentation.moviedetail.MovieDetailAction.LoadEpisodesAction
import dev.alks.movieshow.presentation.moviedetail.MovieDetailResult.LoadEpisodesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailActionProcessHolder @Inject constructor(
    private val movieDetailUseCase: MovieDetailUseCase
) {

    private val episodesProcessor =
        ObservableTransformer<LoadEpisodesAction, LoadEpisodesResult> { action ->
            action.flatMap {
                movieDetailUseCase.invoke(it.movieId)
                    .map{ episodes -> LoadEpisodesResult.Success(episodes.toMutableList()) }
                    .cast(LoadEpisodesResult::class.java)
                    .onErrorReturn(LoadEpisodesResult::Error)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(LoadEpisodesResult.Loading)
            }
        }

    private val movieDetailProcessor =
        ObservableTransformer<MovieDetailAction.SetMovieDetailAction, MovieDetailResult.SetMovieDetailResult> { action ->
            action.map { movie -> MovieDetailResult.SetMovieDetailResult.Success(movie = movie.movie) }
                .cast(MovieDetailResult.SetMovieDetailResult::class.java)
                .onErrorReturn(MovieDetailResult.SetMovieDetailResult::Error)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(MovieDetailResult.SetMovieDetailResult.Loading)
        }

    internal var actionProcessor =
        ObservableTransformer<MovieDetailAction, MovieDetailResult> { action ->
            action.publish { shared ->
                Observable.merge(
                    shared.ofType(LoadEpisodesAction::class.java).compose(episodesProcessor),
                    shared.ofType(MovieDetailAction.SetMovieDetailAction::class.java)
                        .compose(movieDetailProcessor)
                ).mergeWith(shared
                    .filter { v ->
                        v !is LoadEpisodesAction && v !is MovieDetailAction.SetMovieDetailAction
                    }.flatMap { error ->
                        Observable.error(
                            IllegalStateException("Unknown Action type : $error")
                        )
                    }

                )
            }
        }
}