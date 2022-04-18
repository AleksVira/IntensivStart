package ru.androidschool.intensiv.presentation.movie_details.viewmodel

import android.content.Context
import ru.androidschool.intensiv.common.prepare
import ru.androidschool.intensiv.core.CoreViewModel
import ru.androidschool.intensiv.data.database.entity.ActorDbEntity
import ru.androidschool.intensiv.data.database.entity.MovieDbEntity
import ru.androidschool.intensiv.data.mapper.ActorMapper
import ru.androidschool.intensiv.data.mapper.DbActorsMapper
import ru.androidschool.intensiv.data.mapper.DbMovieDetailsMapper
import ru.androidschool.intensiv.data.mapper.MovieDetailsMapper
import ru.androidschool.intensiv.data.repositoryImpl.LocalMovieRepository
import ru.androidschool.intensiv.data.repositoryImpl.RemoteMovieRepository
import ru.androidschool.intensiv.domain.interactor.MovieDetailInteractor
import ru.androidschool.intensiv.presentation.movie_details.ActorInfoItem
import timber.log.Timber

class MovieDetailsViewModel(
    movieId: Int,
    context: Context
) : CoreViewModel<MovieDetailsViewState, MovieDetailsViewEvent>() {

    private val movieDetailInteractor =
        MovieDetailInteractor(RemoteMovieRepository(), LocalMovieRepository(context))
    private val movieDetailsInfoMapper = MovieDetailsMapper()
    private val dbMovieDetailsMapper = DbMovieDetailsMapper()
    private val actorMapper = ActorMapper()
    private val dbActorsMapper = DbActorsMapper()

    private lateinit var currentDetailsForDb: MovieDbEntity
    private lateinit var currentActors: List<ActorDbEntity>
    private var isSelectedMovie = false

    init {
        fetchDetailMovieInfo(movieId)
        fetchActors(movieId)
        checkSelectedMovies(movieId)
    }

    private fun checkSelectedMovies(movieId: Int) {
        movieDetailInteractor.checkSavedMovieById(movieId)
            .prepare()
            .subscribe { result ->
                state.value = MovieDetailsViewState.StateIcon(result)
                Timber.d("MyTAG_MovieDetailsViewModel_checkSelectedMovies(): $result")
            }.autoDispose()

    }

    override fun perform(viewEvent: MovieDetailsViewEvent) {
        when (viewEvent) {
            MovieDetailsViewEvent.IconClicked -> changeSelectedState()
        }
    }

    private fun changeSelectedState() {
        if (!isSelectedMovie) {
            if (::currentDetailsForDb.isInitialized && this::currentActors.isInitialized) {
                movieDetailInteractor.saveToDb(currentDetailsForDb, currentActors)
                    .prepare()
                    .subscribe {
                        isSelectedMovie = true
                        state.value = MovieDetailsViewState.StateIcon(isSelectedMovie)
                    }.autoDispose()
            }
        } else {
            movieDetailInteractor.deleteMovieById(currentDetailsForDb.movieId)
                .prepare()
                .subscribe {
                    isSelectedMovie = false
                    state.value = MovieDetailsViewState.StateIcon(isSelectedMovie)
                    Timber.d("MyTAG_MovieDetailsViewModel_changeSelectedState(): DELETED! ${currentDetailsForDb.movieId}")
                }.autoDispose()
        }
    }

    private fun fetchDetailMovieInfo(movieId: Int) {
        movieDetailInteractor.getMovieInfoById(movieId)
            .prepare()
            .doOnError {
                Timber.d("MyTAG_MovieDetailsFragment_fetchDetailMovieInfo(): $it")
            }
            .subscribe { response ->
                movieDetailsInfoMapper.mapTo(response)
                    .also { movieDetail ->
                        currentDetailsForDb = dbMovieDetailsMapper.mapTo(movieDetail)
                        state.value = MovieDetailsViewState.StateMovieData(movieDetail)
                    }
            }
            .autoDispose()
    }

    private fun fetchActors(movieId: Int) {
        movieDetailInteractor.getPersonsByMovieId(movieId)
            .prepare()
            .doOnSubscribe {
                commands.onNext(ShowProgress())
            }
            .doFinally {
                commands.onNext(HideProgress())
            }
            .subscribe { movieCreditsResponse ->
                val newActorsListEntities = movieCreditsResponse.cast?.map { cast ->
                    actorMapper.mapTo(cast)
                } ?: listOf()
                currentActors = dbActorsMapper.mapTo(newActorsListEntities)
                newActorsListEntities.map { singleActorInfo ->
                    ActorInfoItem(
                        singleActorInfo,
                        onClick = this::printPersonName
                    )
                }.let { actorsList ->
                    state.value = MovieDetailsViewState.StatePersonsData(actorsList)
                }
            }
            .autoDispose()
    }

    private fun printPersonName(name: String) {
        Timber.d("MyTAG_MovieDetailsViewModel_printPersonName(): $name")
    }

}