package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.serialization.ExperimentalSerializationApi
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.common.prepare
import ru.androidschool.intensiv.data.mapper.ActorMapper
import ru.androidschool.intensiv.data.mapper.MovieDetailsInfoMapper
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.databinding.FragmentMovieDetailsBinding
import ru.androidschool.intensiv.domain.entity.ActorInfoEntity
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity
import timber.log.Timber

@ExperimentalSerializationApi
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val compositeDisposable = CompositeDisposable()
    private val movieDetailsInfoMapper = MovieDetailsInfoMapper()
    private val actorMapper = ActorMapper()

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val detailsAdapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.arrowBackImage.setOnClickListener { onBackPressed() }
        args.movieId.let { movieId ->
            fetchDetailMovieInfo(movieId)
            fetchCredits(movieId)
        }
    }

    private fun bindDetails(movieDetails: MovieDetailsEntity) {
        with(binding) {
            detailImage.loadImage(movieDetails.movieImageUrl)
            detailNameText.text = movieDetails.movieName
            customRating.setRating(movieDetails.movieRating)
            detailDescriptionText.text = movieDetails.movieDescription
            studioNameText.text = movieDetails.studioName
            genreText.text = movieDetails.genre
            yearText.text = movieDetails.year
        }
    }

    private fun fetchDetailMovieInfo(movieId: Int) {
        MovieApiClient.apiClient.getMovieInfoById(movieId)
            .prepare()
            .doOnError {
                Timber.d("MyTAG_MovieDetailsFragment_fetchDetailMovieInfo(): $it")
            }
            .subscribe { response ->
                movieDetailsInfoMapper.mapTo(response)
                    .also { movieDetail ->
                        bindDetails(movieDetail)
                    }
            }
            .let {
                compositeDisposable.addAll(it)
            }
    }

    private fun fetchCredits(movieId: Int) {
        MovieApiClient.apiClient.getMoviePersonsById(movieId)
            .prepare()
            .doOnSubscribe {
                binding.progressView.visibility = View.VISIBLE
                binding.detailActorsList.visibility = View.GONE
            }
            .doFinally {
                binding.progressView.visibility = View.GONE
                binding.detailActorsList.visibility = View.VISIBLE
            }
            .subscribe { response ->
                val newActorsListItems = response.cast?.map { cast ->
                    ActorInfoItem(
                        content = actorMapper.mapTo(cast),
                        onClick = { name ->
                            Timber.d("MyTAG_MovieDetailsFragment_bindDetails(): $name")
                        }
                    )
                } ?: listOf()
                binding.detailActorsList.adapter = detailsAdapter.apply {
                    addAll(newActorsListItems)
                }
            }
            .let {
                compositeDisposable.addAll(it)
            }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }

}
