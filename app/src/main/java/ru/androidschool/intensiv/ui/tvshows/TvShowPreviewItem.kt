package ru.androidschool.intensiv.ui.tvshows

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.databinding.ItemTvShowBinding
import ru.androidschool.intensiv.domain.entity.TvShowEntity

class TvShowPreviewItem(
    private val content: TvShowEntity,
    private val onClick: (tvShowEntity: TvShowEntity) -> Unit
) : BindableItem<ItemTvShowBinding>() {

    override fun getLayout() = R.layout.item_tv_show

    override fun bind(view: ItemTvShowBinding, position: Int) {
        view.tvShowDescription.text = content.title
        view.tvShowRating.rating = content.rating

        view.tvShowImage.setOnClickListener {
            onClick.invoke(content)
        }
        view.tvShowImage
//            .loadImage("https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg")
            .loadImage(content.posterUrl)
    }

    override fun initializeViewBinding(v: View): ItemTvShowBinding = ItemTvShowBinding.bind(v)
}
