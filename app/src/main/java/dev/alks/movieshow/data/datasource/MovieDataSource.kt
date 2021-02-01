package dev.alks.movieshow.data.datasource

import javax.inject.Inject

class MovieDataSource @Inject constructor(
    private val service: MovieService
) {
    fun searchMovie(searchTerm: String) =
        service.searchMovies(searchTerm)

    fun getMovieDetail(movieId: Int) =
        service.getMovieDetail(movieId)

}