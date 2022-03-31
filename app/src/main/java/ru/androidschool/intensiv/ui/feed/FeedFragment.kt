package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
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
import ru.androidschool.intensiv.data.mapper.MovieDtoMapper
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.domain.entity.MovieListToShow
import timber.log.Timber

@ExperimentalSerializationApi
class FeedFragment : Fragment(R.layout.feed_fragment) {

    private val compositeDisposable = CompositeDisposable()
    private val movieDtoMapper = MovieDtoMapper()

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
        fetchAllMovies()
    }

    private fun fetchAllMovies() {
        val getNowPlayingMovies: Single<MoviesListResponse<MovieDto>> =
            MovieApiClient.apiClient.getNowPlayingMoviesResponse()
                .onErrorReturn {
                    Timber.d("MyTAG_FeedFragment_fetchAllMovies() ERROR in ${R.string.recommended}")
                    MoviesListResponse()
                }
        val getUpcomingMovies: Single<MoviesListResponse<MovieDto>> =
            MovieApiClient.apiClient.getUpcomingMoviesResponse()
                .onErrorReturn {
                    Timber.d("MyTAG_FeedFragment_fetchAllMovies() ERROR in ${R.string.upcoming}")
                    MoviesListResponse()
                }
        val getPopularMovies: Single<MoviesListResponse<MovieDto>> =
            MovieApiClient.apiClient.getPopularMoviesResponse()
                .onErrorReturn {
                    Timber.d("MyTAG_FeedFragment_fetchAllMovies() ERROR in ${R.string.popular}")
                    MoviesListResponse()
                }

        Single.zip(
            getNowPlayingMovies,
            getUpcomingMovies,
            getPopularMovies
        ) { nowPlayingList: MoviesListResponse<MovieDto>,
            upcomingList: MoviesListResponse<MovieDto>,
            popularList: MoviesListResponse<MovieDto> ->
            UnitedMoviesList(nowPlayingList, upcomingList, popularList)
        }
            .prepare()
            .doOnSubscribe {
                binding.progressView.visibility = VISIBLE
            }
            .doFinally {
                binding.progressView.visibility = GONE
            }
            .subscribe { response ->
                val nowPlayingMovies = response.nowPlayingList.results ?: listOf()
                val upcomingMovies = response.upcomingList.results ?: listOf()
                val popularMovies = response.popularList.results ?: listOf()

                val nowPlayingItems = convertToItems(R.string.recommended, nowPlayingMovies.map {
                    movieDtoMapper.mapTo(it)
                })
                val upcomingItems = convertToItems(R.string.upcoming, upcomingMovies.map {
                    movieDtoMapper.mapTo(it)
                })
                val popularItems = convertToItems(R.string.popular, popularMovies.map {
                    movieDtoMapper.mapTo(it)
                })
                adapter.apply { addAll(nowPlayingItems + upcomingItems + popularItems) }
            }
            .let { compositeDisposable.addAll(it) }
    }

    private fun convertToItems(@StringRes header: Int, nowPlayingList: List<MovieEntity>) =
        listOf(
            MainCardContainer(
                header,
                nowPlayingList.map {
                    MovieItem(it) { movie -> openMovieDetails(movie) }
                }
            )
        )


    private fun openMovieDetails(movieEntity: MovieEntity) {
        val action =
            FeedFragmentDirections.actionHomeDestToMovieDetailsFragment(movieEntity.movieId)
        findNavController().navigate(action)
    }

    private fun startSearch(initialString: String) {
        Timber.d("MyTAG_FeedFragment_startSearch(): INITIAL = $initialString")
        MovieApiClient.apiClient.searchMovieByTitle(initialString)
            .prepare()
            .doOnSubscribe {
                binding.progressView.visibility = VISIBLE
            }
            .doFinally {
                binding.progressView.visibility = GONE
            }
            .doOnError {
                Timber.d("MyTAG_FeedFragment_startSearch(): $it")
            }
            .doOnNext { response ->
                if (initialString == formSearchEditText.text.toString()) {
                    Timber.d("MyTAG_FeedFragment_startSearch(): CAN SHOW!")
                    val moviesDtoList = response.results ?: listOf()
                    val moviesEntityList = moviesDtoList.map { movieDto ->
                        movieDtoMapper.mapTo(movieDto)
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
