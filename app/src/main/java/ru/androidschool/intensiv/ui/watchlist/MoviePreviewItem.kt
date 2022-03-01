package ru.androidschool.intensiv.ui.watchlist

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.databinding.ItemSmallBinding

class MoviePreviewItem(
    private val content: Movie,
    private val onClick: (movie: Movie) -> Unit
) : BindableItem<ItemSmallBinding>() {

    override fun getLayout() = R.layout.item_small

    override fun bind(view: ItemSmallBinding, position: Int) {
        view.imagePreview.setOnClickListener {
            onClick.invoke(content)
        }
        view.imagePreview
            .loadImage("https://www.kinopoisk.ru/images/film_big/1143242.jpg")
    }

    override fun initializeViewBinding(v: View): ItemSmallBinding = ItemSmallBinding.bind(v)
}
