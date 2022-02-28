package ru.androidschool.intensiv.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import ru.androidschool.intensiv.R


class CustomFiveStarsRating @JvmOverloads
constructor(
    private val ctx: Context,
    private val attributeSet: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
) : ViewGroup(ctx, attributeSet, defStyleAttr) {


    companion object {
        const val DELTA = 0.125
        const val QUARTER = 0.25
        const val HALF = 0.5
        const val THREE_QUARTERS = 0.75
    }

    private var ratingInPercents: Float
    private var starSize: Float
    private var marginBetweenStars: Int

    @DrawableRes
    val defaultStar: Int = R.drawable.custom_rating_stars

    private var numberOfStars: Int = 1
    private var emptyStar: Int = 0
    private var quarterFilledStar: Int = 0
    private var halfFilledStar: Int = 0
    private var threeQuartersFilledStar: Int = 0
    private var fullFilledStar: Int = 0

    init {
        setWillNotDraw(true)

        ctx.obtainStyledAttributes(attributeSet, R.styleable.CustomFiveStarsRating).apply {
            try {
                numberOfStars = getInt(R.styleable.CustomFiveStarsRating_numberOfStars, 1)
                starSize = getDimension(R.styleable.CustomFiveStarsRating_starSize, 0f)
                marginBetweenStars = (starSize / 4).toInt()
                ratingInPercents = getFloat(R.styleable.CustomFiveStarsRating_startRating, 0f)
                emptyStar = getResourceId(
                    R.styleable.CustomFiveStarsRating_emptyStarImage,
                    defaultStar
                )
                quarterFilledStar = getResourceId(
                    R.styleable.CustomFiveStarsRating_quarterFilledStarImage,
                    defaultStar
                )
                halfFilledStar = getResourceId(
                    R.styleable.CustomFiveStarsRating_halfFilledStarImage,
                    defaultStar
                )
                threeQuartersFilledStar = getResourceId(
                    R.styleable.CustomFiveStarsRating_threeQuarterFilledStarImage,
                    defaultStar
                )
                fullFilledStar = getResourceId(
                    R.styleable.CustomFiveStarsRating_fullFilledStarImage,
                    defaultStar
                )
            } finally {
                recycle()
            }
        }

        for (i in 1..numberOfStars) {
            val nextChild = ImageView(ctx)
            addView(nextChild)
        }
        setStars(ratingInPercents)
    }

    private fun setStars(ratingInPercents: Float) {
        val filledStars = ratingInPercents / 100 * numberOfStars
        val fullyFilled = filledStars.toInt()
        val partiallyFilled: Float = filledStars - fullyFilled

        forEachIndexed { index, child ->
            when {
                index + 1 <= fullyFilled -> {
                    child.setBackgroundResource(fullFilledStar)
                }
                (index + 1 == fullyFilled + 1) -> {
                    when {
                        partiallyFilled < DELTA -> child.setBackgroundResource(emptyStar)
                        partiallyFilled < QUARTER + DELTA -> child.setBackgroundResource(quarterFilledStar)
                        partiallyFilled < HALF + DELTA -> child.setBackgroundResource(halfFilledStar)
                        partiallyFilled < THREE_QUARTERS + DELTA -> child.setBackgroundResource(threeQuartersFilledStar)
                        else -> child.setBackgroundResource(fullFilledStar)
                    }
                }
                else -> child.setBackgroundResource(emptyStar)
            }
            val params = LinearLayout.LayoutParams(starSize.toInt(), starSize.toInt())
            child.layoutParams = params
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = starSize.toInt()
        var currentWidth = 0

        children.forEach { child ->
            measureChildWithMargins(child,
                widthMeasureSpec,
                currentWidth,
                heightMeasureSpec,
                height)
            currentWidth += child.measuredHeight + marginBetweenStars
            height = kotlin.math.max(height, child.measuredHeight)
        }
        currentWidth += paddingBottom + paddingTop - marginBetweenStars
        height += paddingStart + paddingEnd
        setMeasuredDimension(
            resolveSize(currentWidth, widthMeasureSpec),
            resolveSize(height, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var currentLeft = paddingLeft
        children.forEach { child ->
            val currentRight = currentLeft + child.measuredWidth
            child.layout(currentLeft, paddingTop, currentRight, paddingTop + child.measuredHeight)
            currentLeft = currentRight + marginBetweenStars
        }
    }

    override fun generateDefaultLayoutParams() = MarginLayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT
    )

    override fun generateLayoutParams(attrs: AttributeSet?) =
        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    fun setRating(newRating: Float) {
        ratingInPercents = newRating
        setStars(ratingInPercents)
    }

}