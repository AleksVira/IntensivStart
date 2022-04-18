package ru.androidschool.intensiv.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.androidschool.intensiv.common.delegate
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.androidschool.intensiv.common.onNext

abstract class CoreViewModel<S : CoreViewState, E : CoreViewEvent>(
    initialState: S? = null,
    mutableLiveData: MutableLiveData<S> = MutableLiveData<S>()
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    abstract fun perform(viewEvent: E)

    protected val state = mutableLiveData.apply {
        initialState?.let { onNext(it) }
    }

    protected var stateData: S by state.delegate()

    val commands = CommandsQueue<ViewCommand>()

    fun state(): LiveData<S> = state



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    protected fun Disposable.autoDispose() = addDisposable(this)

    protected fun updateState(newState: S) {
        stateData = newState
    }

    protected fun updateStateFromIo(newState: S) {
        state.postValue(newState)
    }



}
