package ru.androidschool.intensiv.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.afterTextChanged
import ru.androidschool.intensiv.databinding.SearchToolbarBinding
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val compositeDisposable = CompositeDisposable()

    lateinit var binding: SearchToolbarBinding

    private val searchStringSubject = PublishSubject.create<String>()
    private val localSubject = PublishSubject.create<String>()

    private var hint: String = ""
    private var isCancelVisible: Boolean = true

    companion object {
        const val MIN_LENGTH = 3
    }

    init {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.SearchBar).apply {
                hint = getString(R.styleable.SearchBar_hint).orEmpty()
                isCancelVisible = getBoolean(R.styleable.SearchBar_cancel_visible, true)
                recycle()
            }
        }

        localSubject
            .filter { it -> it.length > MIN_LENGTH }
            .toFlowable(BackpressureStrategy.LATEST)
            .distinctUntilChanged()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .map { string ->
                Timber.d("MyTAG_SearchBar_onAttachedToWindow(): $string")
                val cleared = string.trim().replace("\\s+".toRegex(), " ")
                if (string != cleared) {
                    binding.searchEditText.setText(cleared)
                    binding.searchEditText.setSelection(cleared.length)
                } else {
                    searchStringSubject.onNext(string)
                }
            }.subscribe()
            .let { compositeDisposable.add(it) }
    }

    fun onNewStringObservable(): Observable<String> = searchStringSubject.hide()

    fun setText(text: String?) {
        binding.searchEditText.setText(text)
    }

    fun clear() {
        binding.searchEditText.setText("")
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = SearchToolbarBinding.inflate(LayoutInflater.from(context), this, true)
        binding.searchEditText.hint = hint
        binding.deleteTextButton.setOnClickListener {
            binding.searchEditText.text.clear()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.searchEditText.afterTextChanged { text ->
            if (!text.isNullOrEmpty() && !binding.deleteTextButton.isVisible) {
                binding.deleteTextButton.visibility = View.VISIBLE
            }
            if (text.isNullOrEmpty() && binding.deleteTextButton.isVisible) {
                binding.deleteTextButton.visibility = View.GONE
            }
            localSubject.onNext(text.toString())
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compositeDisposable.clear()
    }
}
