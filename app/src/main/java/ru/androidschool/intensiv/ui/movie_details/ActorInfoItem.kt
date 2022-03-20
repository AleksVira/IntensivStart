package ru.androidschool.intensiv.ui.movie_details

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.databinding.ItemActorInfoBinding
import ru.androidschool.intensiv.domain.entity.ActorInfoEntity

class ActorInfoItem(
    private val content: ActorInfoEntity,
    private val onClick: (actorName: String) -> Unit
) : BindableItem<ItemActorInfoBinding>() {

    companion object {
        private const val ACTOR_AVATAR_SIZE = 60
    }

    override fun getLayout(): Int = R.layout.item_actor_info

    override fun initializeViewBinding(v: View) = ItemActorInfoBinding.bind(v)

    override fun bind(view: ItemActorInfoBinding, position: Int) {
        view.actorImage.setOnClickListener {
            onClick.invoke(content.fullName)
        }
        view.actorFullNameText.text = content.fullName
        Picasso.get()
            .load(content.imageUrl)
            .resize(ACTOR_AVATAR_SIZE, 0)
            .centerCrop()
            .into(view.actorImage)
    }
}
