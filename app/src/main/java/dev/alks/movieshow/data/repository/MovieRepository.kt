package dev.alks.movieshow.data.repository

import dev.alks.movieshow.data.datasource.MovieDataSource
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieDataSource: MovieDataSource) {

    fun searchMovie(searchTerm: String) =
        movieDataSource.searchMovie(searchTerm)

    fun getMovieDetail(movieId: Int) =
        movieDataSource.getMovieDetail(movieId)


}