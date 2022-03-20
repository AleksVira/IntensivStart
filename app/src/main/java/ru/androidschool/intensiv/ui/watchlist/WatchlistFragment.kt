package ru.androidschool.intensiv.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.common.prepare
import ru.androidschool.intensiv.data.mapper.MovieDetailsMapper
import ru.androidschool.intensiv.data.repository.SelectedMovieRepository
import ru.androidschool.intensiv.databinding.FragmentWatchlistBinding

class WatchlistFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val movieDetailsInfoMapper = MovieDetailsMapper()

    private val compositeDisposable = CompositeDisposable()
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var repository: SelectedMovieRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = SelectedMovieRepository(requireContext())

        binding.moviesRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.moviesRecyclerView.adapter = adapter.apply { addAll(listOf()) }

        repository.getSelectedMovies()
            .prepare()
            .subscribe {
                it
                    .map { movieWithActors ->
                        MoviePreviewItem(movieWithActors) { movie -> }
                    }.toList().also { moviesFromDb ->
                        binding.moviesRecyclerView.adapter = adapter.apply { addAll(moviesFromDb) }
                    }
            }.let {
                compositeDisposable.addAll(it)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = WatchlistFragment()
    }
}
