package ru.androidschool.intensiv.ui.movie_details

import android.view.View
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.ActorInfoEntity
import ru.androidschool.intensiv.databinding.ItemActorInfoBinding

class ActorInfoItem(
    private val content: ActorInfoEntity,
    private val onClick: (actorName: String) -> Unit
) : BindableItem<ItemActorInfoBinding>() {

    companion object {
        private const val ACTOR_AVATAR_SIZE = 60
    }

    override fun createViewHolder(itemView: View): GroupieViewHolder<ItemActorInfoBinding> {
        itemView.findViewById<ShapeableImageView>(R.id.actor_image).setOnClickListener {
            onClick.invoke(content.fullName)
        }
        return super.createViewHolder(itemView)
    }

    override fun getLayout(): Int = R.layout.item_actor_info

    override fun initializeViewBinding(v: View) = ItemActorInfoBinding.bind(v)

    override fun bind(view: ItemActorInfoBinding, position: Int) {
        view.actorFullNameText.text = content.fullName
        Picasso.get()
            .load(content.imageUrl)
            .resize(ACTOR_AVATAR_SIZE, 0)
            .centerCrop()
            .into(view.actorImage)
    }
}
