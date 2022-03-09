package ru.androidschool.intensiv.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.databinding.FragmentSearchBinding
import ru.androidschool.intensiv.domain.entity.MovieEntity

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = requireNotNull(_binding)
    private val searchBinding get() = requireNotNull(_searchBinding)

    private val args: SearchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        _searchBinding = FeedHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.moviesRecyclerView.adapter = adapter.apply { addAll(listOf()) }
        searchBinding.searchToolbar.setText(args.initialString)
        val initialList = args.movieList.movies
        updateList(initialList)
    }

    private fun updateList(searchMovieList: List<MovieEntity>) {
        val movieList = searchMovieList.map {
            SearchMoviePreviewItem(it) {}
        }
        adapter.apply { addAll(movieList) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchBinding = null
    }
}
