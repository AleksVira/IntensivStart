package ru.androidschool.intensiv.common

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.loadImage(imageUrl: String) {
        Picasso.get()
            .load(imageUrl)
            .into(this)
}
