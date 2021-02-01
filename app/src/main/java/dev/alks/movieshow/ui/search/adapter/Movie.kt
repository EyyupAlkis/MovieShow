package dev.alks.movieshow.ui.search.adapter

import androidx.databinding.ObservableField
import dev.alks.movieshow.data.datasource.model.Show

class Movie(movie: Show) {
    val id = ObservableField<Int>(0)
    var movieName = ObservableField<String>()
    var definition = ObservableField<String>()
    var rating = ObservableField<Double?>()
    val genres = ObservableField<List<String>>()
    var smallImageUrl = ObservableField<String?>()
    var originalImageUrl = ObservableField<String?>()

    init {
        id.set(movie.id)
        movieName.set(movie.name)
        definition.set(movie.summary)
        rating.set(movie.rating.average)
        genres.set(movie.genres)
        smallImageUrl.set(movie.image?.medium ?: "")
        originalImageUrl.set(movie.image?.original ?: "")
    }


}
