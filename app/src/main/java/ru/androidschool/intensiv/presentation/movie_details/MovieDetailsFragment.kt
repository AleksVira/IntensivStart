package ru.androidschool.intensiv.presentation.movie_details

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
import ru.androidschool.intensiv.common.setThrottleClickListener
import ru.androidschool.intensiv.data.database.entity.ActorDbEntity
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity
import ru.androidschool.intensiv.data.mapper.ActorMapper
import ru.androidschool.intensiv.data.mapper.DbActorsMapper
import ru.androidschool.intensiv.data.mapper.DbMovieDetailsMapper
import ru.androidschool.intensiv.data.mapper.MovieDetailsMapper
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.repositoryImpl.SelectedMovieRepository
import ru.androidschool.intensiv.databinding.FragmentMovieDetailsBinding
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity
import timber.log.Timber
import java.util.*

@ExperimentalSerializationApi
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val compositeDisposable = CompositeDisposable()
    private val movieDetailsInfoMapper = MovieDetailsMapper()
    private val actorMapper = ActorMapper()
    private val dbMovieDetailsMapper = DbMovieDetailsMapper()
    private val dbActorsMapper = DbActorsMapper()
    private var isSelectedMovie = false

    private lateinit var currentDetailsForDb: MovieDbEntity
    private lateinit var currentActors: List<ActorDbEntity>

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val detailsAdapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val args: MovieDetailsFragmentArgs by navArgs()

    private lateinit var repository: SelectedMovieRepository

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
        repository = SelectedMovieRepository(requireContext())
        binding.arrowBackImage.setOnClickListener { onBackPressed() }
        args.movieId.let { movieId ->
            val lang = Locale.getDefault().language
            fetchDetailMovieInfo(movieId, lang)
            fetchActors(movieId, lang)
            checkSelectedMovies(movieId)
        }
        binding.likeImage.setThrottleClickListener {
            changeSelectedState()
        }
    }

    private fun changeSelectedState() {
        if (!isSelectedMovie) {
            if (::currentDetailsForDb.isInitialized && this::currentActors.isInitialized) {
                repository.saveToDb(currentDetailsForDb, currentActors)
                    .prepare()
                    .doOnSubscribe {
                        binding.progressView.visibility = View.VISIBLE
                    }
                    .doFinally {
                        binding.progressView.visibility = View.GONE
                    }
                    .subscribe {
                        setNewIconSelectedState(!isSelectedMovie)
                    }.let {
                        compositeDisposable.addAll(it)
                    }
            }
        } else {
            repository.deleteMovieById(currentDetailsForDb.movieId)
                .prepare()
                .subscribe {
                    Timber.d("MyTAG_MovieDetailsFragment_changeSelectedState(): DELETED! ${currentDetailsForDb.movieId}")
                }.let {
                    compositeDisposable.addAll(it)
                }
            setNewIconSelectedState(!isSelectedMovie)
        }
    }

    private fun setNewIconSelectedState(isSelected: Boolean) {
        isSelectedMovie = isSelected
        val drawableRes = if (isSelected) {
            R.drawable.ic_like_filled
        } else {
            R.drawable.ic_like
        }
        binding.likeImage.setImageResource(drawableRes)
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

    private fun fetchDetailMovieInfo(movieId: Int, lang: String) {
        MovieApiClient.apiClient.getMovieInfoById(movieId, lang)
            .prepare()
            .doOnError {
                Timber.d("MyTAG_MovieDetailsFragment_fetchDetailMovieInfo(): $it")
            }
            .subscribe { response ->
                movieDetailsInfoMapper.mapTo(response)
                    .also { movieDetail ->
                        currentDetailsForDb = dbMovieDetailsMapper.mapTo(movieDetail)
                        bindDetails(movieDetail)
                    }
            }
            .let {
                compositeDisposable.addAll(it)
            }
    }

    private fun checkSelectedMovies(movieId: Int) {
        repository.checkSavedMovieById(movieId)
            .prepare()
            .subscribe { result ->
                setNewIconSelectedState(result)
                Timber.d("MyTAG_MovieDetailsFragment_checkSelectedMovies(): $result")
            }.let {
                compositeDisposable.addAll(it)
            }
    }

    private fun fetchActors(movieId: Int, lang: String) {
        MovieApiClient.apiClient.getMoviePersonsById(movieId, lang)
            .prepare()
            .doOnSubscribe {
                binding.progressView.visibility = View.VISIBLE
                binding.detailActorsList.visibility = View.GONE
            }
            .doFinally {
                binding.progressView.visibility = View.GONE
                binding.detailActorsList.visibility = View.VISIBLE
            }
            .subscribe { movieCreditsResponse ->
                val newActorsListEntities = movieCreditsResponse.cast?.map { cast ->
                    actorMapper.mapTo(cast)
                } ?: listOf()
                currentActors = dbActorsMapper.mapTo(newActorsListEntities)
                newActorsListEntities.map { singleActorInfo ->
                    ActorInfoItem(
                        singleActorInfo,
                        onClick = { name ->
                            Timber.d("MyTAG_MovieDetailsFragment_bindDetails(): $name")
                        }
                    )
                }.let { actorsList ->
                    binding.detailActorsList.adapter = detailsAdapter.apply {
                        addAll(actorsList)
                    }
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
