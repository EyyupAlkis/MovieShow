package dev.alks.movieshow.data.domain

import dev.alks.movieshow.data.datasource.model.SearchItem
import dev.alks.movieshow.data.repository.MovieRepository
import io.reactivex.Observable
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : (String) -> Observable<List<SearchItem>> {


    override fun invoke(searchTerm: String) =
        movieRepository.searchMovie(searchTerm)

}