package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.prepare
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.databinding.FragmentTvShowsBinding
import ru.androidschool.intensiv.domain.entity.TvShowEntity
import timber.log.Timber

@ExperimentalSerializationApi
class TvShowsFragment : Fragment(R.layout.fragment_tv_shows) {

    private val compositeDisposable = CompositeDisposable()

    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvShowList.adapter = adapter.apply { addAll(listOf()) }
        fetchTvShowsList()
    }

    @ExperimentalSerializationApi
    private fun fetchTvShowsList() {
        MovieApiClient.apiClient.getTvShowsResponse()
            .prepare()
            .subscribe { response ->
                val tvShowsDtoList = response.results ?: listOf()
                val tvShowsEntityList = tvShowsDtoList.map { tvShowDto ->
                    TvShowEntity(
                        tvShowId = tvShowDto.id ?: 0,
                        title = tvShowDto.name.orEmpty(),
                        voteAverage = tvShowDto.voteAverage ?: 0.0,
                        horizontalPosterUrl = "${BuildConfig.TMDB_RESOURCE_URL}w500${tvShowDto.backdropPath}"
                    )
                }
                val tvShowsList = tvShowsEntityList.map {
                    TvShowPreviewItem(it) {}
                }
                Timber.d("MyTAG_TvShowsFragment_onResponse(): $tvShowsEntityList")
                adapter.apply { addAll(tvShowsList) }
            }
            .let { compositeDisposable.addAll(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance() = TvShowsFragment()
    }
}