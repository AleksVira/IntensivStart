package ru.androidschool.intensiv.presentation.feed

import androidx.annotation.StringRes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.prepare
import ru.androidschool.intensiv.data.mapper.MovieDtoMapper
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

    fun fetchAllMovies() {
        feedInteractor.getFeedMovie()
            .prepare()
            .doOnSubscribe {
                view?.showProgress(true)
            }
            .doFinally {
                view?.showProgress(false)
            }
            .subscribe { response ->
                val nowPlayingMovies = response.nowPlayingList.results ?: listOf()
                val upcomingMovies = response.upcomingList.results ?: listOf()
                val popularMovies = response.popularList.results ?: listOf()

                val nowPlayingItems = convertToItems(R.string.recommended, nowPlayingMovies.map {
                    movieDtoMapper.mapTo(it)
                })
                val upcomingItems = convertToItems(R.string.upcoming, upcomingMovies.map {
                    movieDtoMapper.mapTo(it)
                })
                val popularItems = convertToItems(R.string.popular, popularMovies.map {
                    movieDtoMapper.mapTo(it)
                })
                view?.showMovies(nowPlayingItems + upcomingItems + popularItems)
            }
            .let { compositeDisposable.add(it) }
    }

    private fun convertToItems(@StringRes header: Int, nowPlayingList: List<MovieEntity>) =
        listOf(
            MainCardContainer(
                header,
                nowPlayingList.map {
                    MovieItem(it) { movie -> view?.openMovieDetails(movie) }
                }
            )
        )

    fun startSearch(searchString: String) {
        feedInteractor.searchMovie(searchString)
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


