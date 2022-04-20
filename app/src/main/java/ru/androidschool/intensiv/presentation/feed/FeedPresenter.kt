package ru.androidschool.intensiv.presentation.feed

import androidx.annotation.StringRes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.MoviesType
import ru.androidschool.intensiv.common.prepare
import ru.androidschool.intensiv.data.mapper.MovieDtoMapper
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.domain.entity.MovieListToShow
import ru.androidschool.intensiv.domain.interactor.FeedInteractor
import timber.log.Timber

class FeedPresenter(
    private val feedInteractor: FeedInteractor
) {

    private val compositeDisposable = CompositeDisposable()
    private val movieDtoMapper = MovieDtoMapper()

    var view: FeedView? = null

    fun attachView(view: FeedView) {
        this.view = view
        subscribeForSearchInput(view)
    }

    private fun subscribeForSearchInput(view: FeedView) {
        view.searchBarObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { searchString ->
                Timber.d("MyTAG_FeedFragment_onViewCreated(): WANT SEARCH $searchString")
                view.startSearch(searchString)
            }.let {
                compositeDisposable.add(it)
            }
    }


    fun fetchAllMovies(lang: String) {
        feedInteractor.getFeedMovie(lang)
            .prepare()
            .doOnSubscribe {
                view?.showProgress(true)
            }
            .doFinally {
                view?.showProgress(false)
            }
            .subscribe { response ->
                var nowPlayingItems = emptyList<MainCardContainer>()
                var upcomingItems = emptyList<MainCardContainer>()
                var popularItems = emptyList<MainCardContainer>()

                response.map { entry ->
                    when (entry.key) {
                        MoviesType.NOW_PLAYING -> {
                            nowPlayingItems = movieListConverter(
                                R.string.recommended, entry.value.results ?: listOf()
                            )
                        }
                        MoviesType.UPCOMING -> {
                            upcomingItems = movieListConverter(
                                R.string.upcoming, entry.value.results ?: listOf()
                            )
                        }
                        MoviesType.POPULAR -> {
                            popularItems = movieListConverter(
                                R.string.popular, entry.value.results ?: listOf()
                            )
                        }
                    }
                }
                view?.showMovies(nowPlayingItems + upcomingItems + popularItems)
            }
            .let { compositeDisposable.add(it) }
    }

    private fun movieListConverter(@StringRes header: Int, value: List<MovieDto>) =
        listOf(
            MainCardContainer(
                header,
                value.map { movieDto ->
                    movieDtoMapper.mapTo(movieDto)
                }.map {
                    MovieItem(it) { movie -> view?.openMovieDetails(movie) }
                }
            )
        )


    fun startSearch(searchString: String, lang: String) {
        feedInteractor.searchMovieByTitle(searchString, lang)
            .prepare()
            .doOnSubscribe {
                view?.showProgress(true)
            }
            .doFinally {
                view?.showProgress(false)
            }
            .doOnError {
                Timber.d("MyTAG_FeedFragment_startSearch(): $it")
            }
            .doOnNext { response ->
                val moviesDtoList = response.results ?: listOf()
                val moviesEntityList = moviesDtoList.map { movieDto ->
                    movieDtoMapper.mapTo(movieDto)
                }
                view?.showSearchResult(MovieListToShow(moviesEntityList), searchString)
            }
            .subscribe()
            .let {
                compositeDisposable.add(it)
            }
    }

    fun detachView(view: FeedView) {
        this.view = null
        compositeDisposable.clear()
    }


    interface FeedView {
        fun showMovies(movies: List<MainCardContainer>)
        fun showProgress(isVisible: Boolean)
        fun showError(description: String)
        fun showEmptyMovies()
        fun openMovieDetails(movieEntity: MovieEntity)
        fun startSearch(initialString: String)
        fun showSearchResult(movieListToShow: MovieListToShow, searchString: String)

        fun searchBarObservable(): Observable<String>
    }
}


