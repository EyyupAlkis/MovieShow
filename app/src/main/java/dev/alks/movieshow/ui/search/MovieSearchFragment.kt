package dev.alks.movieshow.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dev.alks.movieshow.R
import dev.alks.movieshow.presentation.basemvi.BaseView
import dev.alks.movieshow.presentation.search.SearchMovieIntent
import dev.alks.movieshow.presentation.search.SearchMovieViewModel
import dev.alks.movieshow.presentation.search.SearchMovieViewState
import dev.alks.movieshow.ui.search.adapter.Movie
import dev.alks.movieshow.ui.search.adapter.MovieSearchAdapter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_movie_search.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieSearchFragment : Fragment(), BaseView<SearchMovieIntent, SearchMovieViewState>,
    MovieSearchAdapter.MovieListener, View.OnClickListener {

    private val adapter: MovieSearchAdapter by lazy {
        MovieSearchAdapter(mutableListOf())
    }

    private val disposable = CompositeDisposable()

    private val searchPublisher = PublishSubject.create<SearchMovieIntent.SearchIntent>()

    @Inject
    lateinit var viewModel: SearchMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        btnSearch.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun bind() {
        disposable.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    override fun intents(): Observable<SearchMovieIntent> {
        return Observable.merge(
            suggestionIntent(),
            searchIntent()
        )
    }

    override fun render(state: SearchMovieViewState) {
        progressSearch.isVisible = state.isLoading

        if (state.movieList?.isNotEmpty() == true) {
            txtErrorSearch.visibility = View.GONE
            recyclerSearch.visibility = View.VISIBLE
             adapter.updateMovieList(state.movieList)
        }

        if (state.error != null) {
            txtErrorSearch.visibility = View.VISIBLE
            recyclerSearch.visibility = View.GONE
        }

    }

    private fun searchIntent(): Observable<SearchMovieIntent.SearchIntent> {
        return searchPublisher
    }

    private fun suggestionIntent(): Observable<SearchMovieIntent.LoadSuggestionIntent> {
        return Observable.just(SearchMovieIntent.LoadSuggestionIntent)
    }


    override fun onMovieClick(movie: Movie) {
        redirectMovieDetailPage(movie)
    }

    private fun redirectMovieDetailPage(movie: Movie) {
        val movieJsonString = Gson().toJson(movie)
        val action = MovieSearchFragmentDirections.actionMovieSearchFragmentToMovieDetailFragment()
        action.movieStringJson = movieJsonString
        findNavController().navigate(action)
    }


    private fun search() {
        val searchTerm = edtTxtSearch.text.toString()
        if (!searchTerm.isNullOrBlank()) {
            searchPublisher.onNext(SearchMovieIntent.SearchIntent(searchTerm))
            edtTxtSearch.setText("")
        } else {
            Toast.makeText(requireContext(), getString(R.string.search_warning), Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            btnSearch-> {
                search()
            }
        }
    }

    private fun initRecyclerView() {
        recyclerSearch.layoutManager = LinearLayoutManager(requireContext())
        adapter.listener = this
        recyclerSearch.adapter = adapter
    }
}