package ru.androidschool.intensiv.presentation.feed

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import kotlinx.serialization.ExperimentalSerializationApi
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.repositoryImpl.NowPlayingMovieRepository
import ru.androidschool.intensiv.data.repositoryImpl.PopularMovieRepository
import ru.androidschool.intensiv.data.repositoryImpl.SearchMovieRepository
import ru.androidschool.intensiv.data.repositoryImpl.UpcomingMovieRepository
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.domain.entity.MovieListToShow
import ru.androidschool.intensiv.domain.interactor.FeedInteractor
import java.util.*

@ExperimentalSerializationApi
class FeedFragment : Fragment(R.layout.feed_fragment), FeedPresenter.FeedView {

    private var _binding: FeedFragmentBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    private val presenter: FeedPresenter by lazy {
        FeedPresenter(FeedInteractor(
            NowPlayingMovieRepository(),
            PopularMovieRepository(),
            UpcomingMovieRepository(),
            SearchMovieRepository(),
        ))
    }

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

    override fun searchBarObservable(): Observable<String> = searchBinding.searchToolbar.onNewStringObservable()

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
        presenter.attachView(this)
        formSearchEditText = searchBinding.searchToolbar.binding.searchEditText
        binding.moviesRecyclerView.adapter = adapter.apply { addAll(listOf()) }
        presenter.fetchAllMovies(Locale.getDefault().language)
    }


    override fun showMovies(movies: List<MainCardContainer>) {
        adapter.apply { addAll(movies) }
    }

    override fun showProgress(isVisible: Boolean) {
        binding.progressView.visibility = if (isVisible) VISIBLE else GONE
    }

    override fun showError(description: String) {
        TODO("Not yet implemented")
    }

    override fun showEmptyMovies() {
        TODO("Not yet implemented")
    }


    override fun openMovieDetails(movieEntity: MovieEntity) {
        val action =
            FeedFragmentDirections.actionHomeDestToMovieDetailsFragment(movieEntity.movieId)
        findNavController().navigate(action)
    }

    override fun startSearch(initialString: String) {
        presenter.startSearch(initialString, Locale.getDefault().language)
    }

    override fun showSearchResult(movieListToShow: MovieListToShow, searchString: String) {
        if (searchString == formSearchEditText.text.toString()) {
            val searchAction = FeedFragmentDirections
                .actionHomeDestToSearchDest(movieListToShow, searchString)
            findNavController().navigate(searchAction)
        } else {
            presenter.startSearch(formSearchEditText.text.toString(), Locale.getDefault().language)
        }
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
        adapter.clear()
        presenter.detachView(this)
    }
}
