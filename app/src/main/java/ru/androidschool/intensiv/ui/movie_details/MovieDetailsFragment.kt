package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.data.network.api.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MovieCreditsResponse
import ru.androidschool.intensiv.data.network.dto.MovieDetailInfoResponse
import ru.androidschool.intensiv.databinding.FragmentMovieDetailsBinding
import ru.androidschool.intensiv.domain.entity.ActorInfoEntity
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity
import timber.log.Timber

@ExperimentalSerializationApi
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

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
        val movieId = args.movieId
        val movieNetworkDetails = MovieApiClient.apiClient.getMovieInfoById(movieId, "ru")
        fetchDetailMovieInfo(movieNetworkDetails)
        val movieCastList = MovieApiClient.apiClient.getMoviePersonsById(movieId, "ru")
        fetchCredits(movieCastList)
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

    private fun fetchDetailMovieInfo(movieNetworkDetails: Call<MovieDetailInfoResponse>) {
        movieNetworkDetails.enqueue(object : Callback<MovieDetailInfoResponse?> {
            override fun onFailure(call: Call<MovieDetailInfoResponse?>, t: Throwable) {
                Timber.d("MyTAG_MovieDetailsFragment_onFailure(): ")
            }

            override fun onResponse(
                call: Call<MovieDetailInfoResponse?>,
                response: Response<MovieDetailInfoResponse?>
            ) {
                response.body()?.let { it ->
                    MovieDetailsEntity(
                        movieImageUrl = "https://image.tmdb.org/t/p/w500${it.backdropPath}",
                        movieName = it.title ?: "",
                        isLiked = false,
                        watchLink = "",
                        movieRating = it.voteAverage?.toFloat() ?: 0F,
                        movieDescription = it.overview ?: "",
                        studioName = it.productionCompanies?.map { company ->
                            company.name
                        }?.joinToString() ?: "",
                        genre = it.genres?.map { genre ->
                            genre.name
                        }?.joinToString()?.replaceFirstChar(Char::titlecase) ?: "",
                        year = it.releaseDate ?: ""
                    )
                }?.also {
                    bindDetails(it)
                }
            }
        })
    }

    private fun fetchCredits(movieCastList: Call<MovieCreditsResponse>) {
        movieCastList.enqueue(object : Callback<MovieCreditsResponse?> {
            override fun onFailure(call: Call<MovieCreditsResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<MovieCreditsResponse?>,
                response: Response<MovieCreditsResponse?>
            ) {
                Timber.d("MyTAG_MovieDetailsFragment_onResponse(): ${response.body()}")
                val newActorsListItems = response.body()?.cast?.map { cast ->
                    Timber.d("MyTAG_MovieDetailsFragment_onResponse(): $cast")
                    ActorInfoItem(
                        content = ActorInfoEntity(
                            imageUrl = "https://image.tmdb.org/t/p/w500${cast.profilePath}",
                            fullName = cast.originalName ?: ""
                        ),
                        onClick = { name ->
                            Timber.d("MyTAG_MovieDetailsFragment_bindDetails(): $name")
                        }
                    )
                } ?: listOf()
                binding.detailActorsList.adapter = detailsAdapter.apply {
                    addAll(newActorsListItems)
                }
            }
        })
    }

    private fun onBackPressed() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
