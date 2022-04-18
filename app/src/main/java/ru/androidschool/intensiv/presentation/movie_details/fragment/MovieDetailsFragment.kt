package ru.androidschool.intensiv.presentation.movie_details.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.serialization.ExperimentalSerializationApi
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.common.setThrottleClickListener
import ru.androidschool.intensiv.core.CoreFragment
import ru.androidschool.intensiv.core.ViewCommand
import ru.androidschool.intensiv.core.observe
import ru.androidschool.intensiv.data.repositoryImpl.LocalMovieRepository
import ru.androidschool.intensiv.databinding.FragmentMovieDetailsBinding
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity
import ru.androidschool.intensiv.presentation.movie_details.ActorInfoItem
import ru.androidschool.intensiv.presentation.movie_details.viewmodel.*

@ExperimentalSerializationApi
class MovieDetailsFragment :
    CoreFragment<MovieDetailsViewState, MovieDetailsViewEvent, MovieDetailsViewModel>(R.layout.fragment_movie_details) {

    private val compositeDisposable = CompositeDisposable()

    private val viewBinding by viewBinding(FragmentMovieDetailsBinding::bind)

    private val detailsAdapter by lazy { GroupAdapter<GroupieViewHolder>() }

    private val args: MovieDetailsFragmentArgs by navArgs()

    private lateinit var repository: LocalMovieRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = LocalMovieRepository(requireContext())
        viewBinding.arrowBackImage.setOnClickListener { onBackPressed() }
        viewBinding.likeImage.setThrottleClickListener {
            viewModel.perform(MovieDetailsViewEvent.IconClicked)
        }
        viewLifecycleOwner.observe(viewModel.commands, ::handleCommands)
    }

    private fun handleCommands(command: ViewCommand) {
        when (command) {
            is ShowProgress -> showActorsLoading(true)
            is HideProgress -> showActorsLoading(false)
        }
    }

    override fun render(state: MovieDetailsViewState) {
        when (state) {
            is MovieDetailsViewState.StateMovieData -> bindDetails(state.movieDetailsEntity)
            is MovieDetailsViewState.StatePersonsData -> bindActors(state.actorsList)
            is MovieDetailsViewState.StateIcon -> setIconState(state.isSaved)
        }
    }

    private fun setIconState(isSaved: Boolean) {
        setNewIconSelectedState(isSaved)
    }

    private fun bindActors(actorsList: List<ActorInfoItem>) {
        viewBinding.detailActorsList.adapter = detailsAdapter.apply {
            addAll(actorsList)
        }
    }

    private fun showActorsLoading(showLoading: Boolean) {
        if (showLoading) {
            viewBinding.progressView.visibility = View.VISIBLE
            viewBinding.detailActorsList.visibility = View.GONE
        } else {
            viewBinding.progressView.visibility = View.GONE
            viewBinding.detailActorsList.visibility = View.VISIBLE
        }
    }

    private fun setNewIconSelectedState(isSelected: Boolean) {
        val drawableRes = if (isSelected) {
            R.drawable.ic_like_filled
        } else {
            R.drawable.ic_like
        }
        viewBinding.likeImage.setImageResource(drawableRes)
    }

    private fun bindDetails(movieDetails: MovieDetailsEntity) {
        with(viewBinding) {
            detailImage.loadImage(movieDetails.movieImageUrl)
            detailNameText.text = movieDetails.movieName
            customRating.setRating(movieDetails.movieRating)
            detailDescriptionText.text = movieDetails.movieDescription
            studioNameText.text = movieDetails.studioName
            genreText.text = movieDetails.genre
            yearText.text = movieDetails.year
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override val viewModel: MovieDetailsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
                    return MovieDetailsViewModel(
                        movieId = args.movieId,
                        context = requireContext()
                    ) as T
                }
                throw IllegalArgumentException("$modelClass is not registered ViewModel")
            }
        }
    }

}
