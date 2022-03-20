package ru.androidschool.intensiv.ui.watchlist

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.data.database.api.MovieWithActors
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.databinding.ItemSmallBinding
import timber.log.Timber

class MoviePreviewItem(
    private val content: MovieWithActors,
    private val onClick: (movieEntity: MovieEntity) -> Unit
) : BindableItem<ItemSmallBinding>() {

    override fun getLayout() = R.layout.item_small

    override fun bind(view: ItemSmallBinding, position: Int) {
        view.apply {
            view.imagePreview.setOnClickListener {}
            if (content.movie.posterUrl.isNotBlank()) {
                Timber.d("MyTAG_MoviePreviewItem_bind(): NOT BLANK")
                view.imagePreview
                    .loadImage(content.movie.posterUrl)
            }
            view.textPreviewName.text = content.movie.title
        }
    }

    override fun initializeViewBinding(v: View): ItemSmallBinding = ItemSmallBinding.bind(v)
}
