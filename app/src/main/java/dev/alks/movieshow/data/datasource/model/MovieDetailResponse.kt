package dev.alks.movieshow.data.datasource.model

import com.google.gson.annotations.SerializedName


data class Episode(
    @SerializedName("id") val id : Int,
    @SerializedName("url") val url : String,
    @SerializedName("name") val name : String,
    @SerializedName("season") val season : Int,
    @SerializedName("number") val number : Int,
    @SerializedName("type") val type : String,
    @SerializedName("airdate") val airdate : String,
    @SerializedName("airtime") val airtime : String?,
    @SerializedName("airstamp") val airstamp : String?,
    @SerializedName("runtime") val runtime : Int,
    @SerializedName("image") val image : Image?,
    @SerializedName("summary") val summary : String?,
    @SerializedName("_links") val _links : Link
)

data class Link(
    val self: Self?
)