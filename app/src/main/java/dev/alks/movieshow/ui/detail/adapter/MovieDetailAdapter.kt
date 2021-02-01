package dev.alks.movieshow.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.alks.movieshow.data.datasource.model.Episode
import dev.alks.movieshow.databinding.EpisodeListSingleItemBinding

class MovieDetailAdapter(private val episodeList: MutableList<Episode>) :
    RecyclerView.Adapter<MovieDetailAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            EpisodeListSingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(episodeList[position])
    }

    override fun getItemCount(): Int = episodeList.size

    fun updateEpisodeList(episodes: MutableList<Episode>) {
        this.episodeList.clear()
        this.episodeList.addAll(episodes)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: EpisodeListSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: Episode) {
            binding.episode = MovieDetail(episode)
        }
    }
}