package dev.alks.movieshow.data.datasource.model

import com.google.gson.annotations.SerializedName


data class SearchItem(
    @SerializedName("score") val score: Double,
    @SerializedName("show") val show: Show
)

data class Show(
    @SerializedName("id") val id: Int,
    @SerializedName("url") val url: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("language") val language: String,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("status") val status: String,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("premiered") val premiered: String?,
    @SerializedName("officialSite") val officialSite: String?,
    @SerializedName("schedule") val schedule: Schedule,
    @SerializedName("rating") val rating: Rating,
    @SerializedName("weight") val weight: Int,
    @SerializedName("network") val network: Network?,
    @SerializedName("webChannel") val webChannel: Network?,
    @SerializedName("externals") val externals: Externals,
    @SerializedName("image") val image: Image?,
    @SerializedName("summary") val summary: String,
    @SerializedName("updated") val updated: Int,
    @SerializedName("_links") val _links: Links
)

data class Network(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: Country
)

data class Schedule(
    @SerializedName("time") val time: String?,
    @SerializedName("days") val days: List<String>?
)

data class Rating(
    @SerializedName("average") val average: Double?
)

data class Externals(
    @SerializedName("tvrage") val tvrage: Int?,
    @SerializedName("thetvdb") val thetvdb: Int?,
    @SerializedName("imdb") val imdb: String?
)

data class Image(
    @SerializedName("medium") val medium: String?,
    @SerializedName("original") val original: String?
)

data class Links(
    @SerializedName("self") val self: Self?,
    @SerializedName("previousepisode") val previousepisode: Self?
)

data class Self(
    @SerializedName("href") val href: String?
)


data class Country(
    @SerializedName("name") val name: String?,
    @SerializedName("code") val code: String?,
    @SerializedName("timezone") val timezone: String?
)
