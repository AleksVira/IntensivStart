package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.data.ActorInfoEntity
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.data.MovieDetailsEntity
import ru.androidschool.intensiv.databinding.FragmentMovieDetailsBinding
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

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
        val movieId = args.movieId
        val movieDetails = MockRepository.getMovieDetails(movieId)
        bindDetails(movieDetails)
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

            val actorsListItems: List<ActorInfoItem> = movieDetails.actorList.map {
                ActorInfoItem(
                    content = ActorInfoEntity(imageUrl = it.imageUrl, fullName = it.fullName),
                    onClick = { name ->
                        Timber.d("MyTAG_MovieDetailsFragment_bindDetails(): $name")
                    }
                )
            }
            detailActorsList.adapter = detailsAdapter.apply {
                addAll(actorsListItems)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
