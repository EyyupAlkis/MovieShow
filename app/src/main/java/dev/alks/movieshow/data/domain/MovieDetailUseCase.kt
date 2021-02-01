package dev.alks.movieshow.data.domain

import dev.alks.movieshow.data.datasource.model.Episode
import dev.alks.movieshow.data.repository.MovieRepository
import io.reactivex.Observable
import javax.inject.Inject

class MovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : (Int) -> Observable<List<Episode>> {

    override fun invoke(movieId: Int) =
        movieRepository.getMovieDetail(movieId)

}