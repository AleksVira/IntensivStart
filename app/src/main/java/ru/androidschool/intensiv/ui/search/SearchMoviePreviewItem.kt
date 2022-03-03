package ru.androidschool.intensiv.ui.search

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.common.loadImage
import ru.androidschool.intensiv.databinding.ItemSearchMoviePreviewBinding
import ru.androidschool.intensiv.domain.entity.MovieEntity

class SearchMoviePreviewItem(
    private val content: MovieEntity,
    private val onClick: (tvShowEntity: MovieEntity) -> Unit
) : BindableItem<ItemSearchMoviePreviewBinding>() {

    override fun getLayout() = R.layout.item_search_movie_preview

    override fun bind(view: ItemSearchMoviePreviewBinding, position: Int) {
        view.searchMovieDescription.text = content.title
        view.searchMovieRating.rating = content.rating

        view.searchMovieImage.setOnClickListener {
            onClick.invoke(content)
        }
        view.searchMovieImage
            .loadImage(content.horizontalPosterUrl)
    }

    override fun initializeViewBinding(v: View): ItemSearchMoviePreviewBinding = ItemSearchMoviePreviewBinding.bind(v)
}
