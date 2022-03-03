package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.afterTextChanged
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MoviesListResponse
import timber.log.Timber

@ExperimentalSerializationApi
class FeedFragment : Fragment(R.layout.feed_fragment) {

    private var _binding: FeedFragmentBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = requireNotNull(_binding)
    private val searchBinding get() = requireNotNull(_searchBinding)

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

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

        searchBinding.searchToolbar.binding.searchEditText.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > MIN_LENGTH) {
                openSearch(it.toString())
            }
        }
        binding.moviesRecyclerView.adapter = adapter.apply { addAll(listOf()) }
        fetchNowPlayingMovies("ru")
        fetchUpcomingMovies("ru")
        fetchPopularMovies("ru")
    }

    private fun fetchNowPlayingMovies(language: String) {
        val getNowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMoviesResponse(language)
        loadAndShowMoviesList(getNowPlayingMovies, R.string.recommended)
    }

    private fun fetchUpcomingMovies(language: String) {
        val getUpcomingMovies = MovieApiClient.apiClient.getUpcomingMoviesResponse(language)
        loadAndShowMoviesList(getUpcomingMovies, R.string.upcoming)
    }

    private fun fetchPopularMovies(language: String) {
        val getPopularMovies = MovieApiClient.apiClient.getPopularMoviesResponse(language)
        loadAndShowMoviesList(getPopularMovies, R.string.popular)
    }

    @ExperimentalSerializationApi
    private fun loadAndShowMoviesList(
        getMoviesListMovies: Call<MoviesListResponse>,
        @StringRes title: Int
    ) {
        getMoviesListMovies.enqueue(object : Callback<MoviesListResponse?> {
            override fun onFailure(call: Call<MoviesListResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<MoviesListResponse?>,
                response: Response<MoviesListResponse?>
            ) {
                val moviesDtoList = response.body()?.results ?: listOf()
                val moviesEntityList = moviesDtoList.map { movieDto ->
                    MovieEntity(
                        movieId = movieDto.id ?: 0,
                        title = movieDto.title ?: "",
                        voteAverage = movieDto.voteAverage ?: 0.0,
                        posterUrl = "https://image.tmdb.org/t/p/w500${movieDto.posterPath}"
                    )
                }

                val recommendedMoviesList = listOf(
                    MainCardContainer(
                        title,
                        moviesEntityList.map { MovieItem(it) { movie -> openMovieDetails(movie) }
                        }
                    )
                )
                adapter.apply { addAll(recommendedMoviesList) }
            }
        })
    }

    private fun openMovieDetails(movieEntity: MovieEntity) {
        val action =
            FeedFragmentDirections.actionHomeDestToMovieDetailsFragment(movieEntity.movieId)
        findNavController().navigate(action)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
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
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_SEARCH = "search"
    }
}
