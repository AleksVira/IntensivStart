package ru.androidschool.intensiv.ui.feed

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.convertToStars
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.databinding.ItemWithTextBinding
import timber.log.Timber

class MovieItem(
    private val content: MovieEntity,
    private val onClick: (movieEntity: MovieEntity) -> Unit
) : BindableItem<ItemWithTextBinding>() {

    override fun getLayout(): Int = R.layout.item_with_text

    override fun bind(view: ItemWithTextBinding, position: Int) {
        view.description.text = content.title
        view.movieRating.rating = content.rating.convertToStars()
        view.content.setOnClickListener {
            onClick.invoke(content)
        }

        view.imagePreview
            .loadImage(content.posterUrl)
    }

    override fun initializeViewBinding(v: View) = ItemWithTextBinding.bind(v)
}
