package dev.alks.movieshow.data.datasource

import dev.alks.movieshow.data.datasource.model.Episode
import dev.alks.movieshow.data.datasource.model.SearchItem
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("search/shows")
    fun searchMovies(@Query("q") searchTerm: String): Observable<List<SearchItem>>

    @GET("shows/{id}/episodes")
    fun getMovieDetail(@Path("id") movieId: Int): Observable<List<Episode>>

}