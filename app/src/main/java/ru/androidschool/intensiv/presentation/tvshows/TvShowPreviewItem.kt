package ru.androidschool.intensiv.presentation.tvshows

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.convertToStars
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.databinding.ItemTvShowBinding
import ru.androidschool.intensiv.domain.entity.TvShowEntity

class TvShowPreviewItem(
    private val content: TvShowEntity,
    private val onClick: (tvShowEntity: TvShowEntity) -> Unit
) : BindableItem<ItemTvShowBinding>() {

    override fun getLayout() = R.layout.item_tv_show

    override fun bind(view: ItemTvShowBinding, position: Int) {
        view.tvShowDescription.text = content.title
        view.tvShowRating.rating = content.rating.convertToStars()
        view.tvShowImage.setOnClickListener {
            onClick.invoke(content)
        }
        view.tvShowImage
            .loadImage(content.horizontalPosterUrl)
    }

    override fun initializeViewBinding(v: View): ItemTvShowBinding = ItemTvShowBinding.bind(v)
}
