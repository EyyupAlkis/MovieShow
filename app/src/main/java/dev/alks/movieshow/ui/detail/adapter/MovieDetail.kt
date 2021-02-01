package dev.alks.movieshow.ui.detail.adapter

import androidx.databinding.ObservableField
import dev.alks.movieshow.data.datasource.model.Episode

class MovieDetail(episode: Episode) {
    var episodeName = ObservableField<String>()
    var episodeDescription = ObservableField<String>()
    var imageUrl = ObservableField<String>()
    var episodeNumber= ObservableField<String>()

    init {
        episodeName.set(episode.name)
        episodeDescription.set(episode.summary)
        imageUrl.set(episode.image?.medium ?: "")
        episodeNumber.set("S:${episode.season} - E:${episode.number}")
    }
}