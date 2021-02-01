package dev.alks.movieshow.ui.detail

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dev.alks.movieshow.R
import dev.alks.movieshow.presentation.basemvi.BaseView
import dev.alks.movieshow.presentation.moviedetail.MovieDetailIntent
import dev.alks.movieshow.presentation.moviedetail.MovieDetailIntent.LoadEpisodesIntent
import dev.alks.movieshow.presentation.moviedetail.MovieDetailViewModel
import dev.alks.movieshow.presentation.moviedetail.MovieDetailViewState
import dev.alks.movieshow.ui.detail.adapter.MovieDetailAdapter
import dev.alks.movieshow.ui.search.adapter.Movie
import dev.alks.movieshow.utils.setHtmlText
import dev.alks.movieshow.utils.setImageUrl
import dev.alks.movieshow.utils.setRate
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailFragment : Fragment(), BaseView<MovieDetailIntent, MovieDetailViewState> {

    private val adapter: MovieDetailAdapter by lazy {
        MovieDetailAdapter(mutableListOf())
    }

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var viewModel: MovieDetailViewModel

    private val movie: Movie by lazy {
        Gson().fromJson(
            arguments?.let { MovieDetailFragmentArgs.fromBundle(it).movieStringJson },
            Movie::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    override fun intents(): Observable<MovieDetailIntent> {
        return Observable.merge(
            episodeIntent(),
            movieDetailIntent()
        )
    }

    override fun render(state: MovieDetailViewState) {
        progressEpisodes.isVisible = state.isLoading

        if (state.episodeList.isNotEmpty()) {
            txtError.isVisible = false
            adapter.updateEpisodeList(state.episodeList)
        }

        if (state.movie != null) {
            setMovieDetail(state.movie)
        }

        if (state.error != null) {
            txtError.isVisible = true
            recyclerEpisodes.isVisible = false
            Toast.makeText(requireContext(), "Error Loading episodes", Toast.LENGTH_LONG).show()
        }
    }


    private fun bind() {
        disposable.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    private fun episodeIntent(): Observable<LoadEpisodesIntent> {
        return Observable.just(LoadEpisodesIntent(movie.id.get()!!))
    }

    private fun movieDetailIntent(): Observable<MovieDetailIntent.SetMovieDetailIntent> {
        return Observable.just(MovieDetailIntent.SetMovieDetailIntent(movie))
    }

    private fun initRecycler() {
        recyclerEpisodes.layoutManager = LinearLayoutManager(requireContext())
        recyclerEpisodes.adapter = adapter
    }

    private fun setMovieDetail(movie: Movie) {
        movie.originalImageUrl.get()?.let { imgMovieDetail.setImageUrl(it) }
        txtTitleMovieDetail.text = movie.movieName.get()
        txtRateMovieDetail.setRate(movie.rating.get())
        movie.definition.get()?.let { txtDescMovieDetail.setHtmlText(it) }
        movieGenres.text = movie.genres.get()?.let { TextUtils.join(", ", it) }
    }

}