package ru.androidschool.intensiv.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import ru.androidschool.intensiv.R

class MovieProgressFormView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    @ColorInt
    private var colorInt = ContextCompat.getColor(context, R.color.selectedControlIndicator)

    init {
        inflate(context, R.layout.movie_view_progress_form, this)
        isClickable = true
        isFocusable = true

        attrs?.let { attributeSet ->
            val array =
                context.obtainStyledAttributes(attributeSet, R.styleable.MovieProgressFormView)
            colorInt = array.getColor(
                R.styleable.MovieProgressFormView_progress_backgroundColor,
                ContextCompat.getColor(context, R.color.buttonGrey)
            )
            array.recycle()
        }
        setBackgroundColor(colorInt)
    }
}