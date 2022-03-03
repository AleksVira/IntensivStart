package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.serialization.ExperimentalSerializationApi
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.prepare
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.domain.entity.MovieListToShow
import timber.log.Timber

@ExperimentalSerializationApi
class FeedFragment : Fragment(R.layout.feed_fragment) {

    private val compositeDisposable = CompositeDisposable()

    private var _binding: FeedFragmentBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = requireNotNull(_binding)
    private val searchBinding get() = requireNotNull(_searchBinding)

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var formSearchEditText: TextView

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedFragmentBinding.inflate(inflater, container, false)
        _searchBinding = FeedHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        formSearchEditText = searchBinding.searchToolbar.binding.searchEditText

        searchBinding.searchToolbar.onNewStringObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { searchString ->
                Timber.d("MyTAG_FeedFragment_onViewCreated(): WANT SEARCH $searchString")
                startSearch(searchString)
            }.let { compositeDisposable.addAll(it) }

        binding.moviesRecyclerView.adapter = adapter.apply { addAll(listOf()) }
        fetchNowPlayingMovies()
        fetchUpcomingMovies()
        fetchPopularMovies()
    }

    private fun fetchNowPlayingMovies() {
        val getNowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMoviesResponse()
        loadAndShowMoviesList(getNowPlayingMovies, R.string.recommended)
    }

    private fun fetchUpcomingMovies() {
        val getUpcomingMovies = MovieApiClient.apiClient.getUpcomingMoviesResponse()
        loadAndShowMoviesList(getUpcomingMovies, R.string.upcoming)
    }

    private fun fetchPopularMovies() {
        val getPopularMovies = MovieApiClient.apiClient.getPopularMoviesResponse()
        loadAndShowMoviesList(getPopularMovies, R.string.popular)
    }

    @ExperimentalSerializationApi
    private fun loadAndShowMoviesList(
        getMoviesListMovies: Single<MoviesListResponse>,
        @StringRes title: Int
    ) {
        getMoviesListMovies
            .prepare()
            .subscribe { response ->
                val moviesDtoList = response.results ?: listOf()
                val moviesEntityList = moviesDtoList.map { movieDto ->
                    MovieEntity(
                        movieId = movieDto.id ?: 0,
                        title = movieDto.title ?: "",
                        voteAverage = movieDto.voteAverage ?: 0.0,
                        posterUrl = "https://image.tmdb.org/t/p/w500${movieDto.posterPath}",
                        horizontalPosterUrl = "https://image.tmdb.org/t/p/w500${movieDto.backdropPath}"
                    )
                }
                val recommendedMoviesList = listOf(
                    MainCardContainer(
                        title,
                        moviesEntityList.map {
                            MovieItem(it) { movie -> openMovieDetails(movie) }
                        }
                    )
                )
                adapter.apply { addAll(recommendedMoviesList) }
            }
            .let { compositeDisposable.addAll(it) }
    }

    private fun openMovieDetails(movieEntity: MovieEntity) {
        val action =
            FeedFragmentDirections.actionHomeDestToMovieDetailsFragment(movieEntity.movieId)
        findNavController().navigate(action)
    }

    private fun startSearch(initialString: String) {
        Timber.d("MyTAG_FeedFragment_startSearch(): INITIAL = $initialString")
        MovieApiClient.apiClient.searchMovieByTitle(initialString)
            .prepare()
            .doOnError {
                Timber.d("MyTAG_FeedFragment_startSearch(): $it")
            }
            .doOnNext { response ->
                if (initialString == formSearchEditText.text.toString()) {
                    Timber.d("MyTAG_FeedFragment_startSearch(): CAN SHOW!")
                    val moviesDtoList = response.results ?: listOf()
                    val moviesEntityList = moviesDtoList.map { movieDto ->
                        MovieEntity(
                            movieId = movieDto.id ?: 0,
                            title = movieDto.title ?: "",
                            voteAverage = movieDto.voteAverage ?: 0.0,
                            posterUrl = "https://image.tmdb.org/t/p/w500${movieDto.posterPath}",
                            horizontalPosterUrl = "https://image.tmdb.org/t/p/w500${movieDto.backdropPath}"
                        )
                    }
                    showSearchResult(MovieListToShow(moviesEntityList), initialString)
                } else {
                    Timber.d("MyTAG_FeedFragment_startSearch(): SHOULD SEARCH AGAIN")
                }
            }
            .subscribe()
            .let { compositeDisposable.add(it) }
    }

    private fun showSearchResult(moviesEntityList: MovieListToShow, initialString: String) {
        val searchAction = FeedFragmentDirections
            .actionHomeDestToSearchDest(moviesEntityList, initialString)
        findNavController().navigate(searchAction)
    }

    override fun onStop() {
        super.onStop()
        searchBinding.searchToolbar.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchBinding = null
        compositeDisposable.clear()
    }
}
