package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.TvShowListResponse
import ru.androidschool.intensiv.databinding.FragmentTvShowsBinding
import ru.androidschool.intensiv.domain.entity.TvShowEntity
import timber.log.Timber

@ExperimentalSerializationApi
class TvShowsFragment : Fragment(R.layout.fragment_tv_shows) {

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
        fetchTvShowsList("ru")
    }

    @ExperimentalSerializationApi
    private fun fetchTvShowsList(language: String) {
        val getTvShows = MovieApiClient.apiClient.getTvShowsResponse(language)
        getTvShows.enqueue(object : Callback<TvShowListResponse?> {
            override fun onFailure(call: Call<TvShowListResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<TvShowListResponse?>,
                response: Response<TvShowListResponse?>
            ) {
                val tvShowsDtoList = response.body()?.results ?: listOf()
                val tvShowsEntityList = tvShowsDtoList.map { tvShowDto ->
                    TvShowEntity(
                        tvShowId = tvShowDto.id ?: 0,
                        title = tvShowDto.name ?: "",
                        voteAverage = tvShowDto.voteAverage ?: 0.0,
                        posterUrl = "https://image.tmdb.org/t/p/w500${tvShowDto.backdropPath}"
                    )
                }

                val tvShowsList = tvShowsEntityList.map {
                    TvShowPreviewItem(it) {}
                }.toList()
                Timber.d("MyTAG_TvShowsFragment_onResponse(): $tvShowsEntityList")
                adapter.apply { addAll(tvShowsList) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = TvShowsFragment()
    }
}