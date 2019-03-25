package com.polohach.unsplash.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

fun ImageView.loadImage(imageUri: String, @DrawableRes placeholder: Int) {
    Glide.with(this.context)
            .load(imageUri)
            .apply(RequestOptions().centerCrop().placeholder(placeholder).error(placeholder))
            .into(this)
}

fun ImageView.loadImageWithListener(imageUri: String,
                                    successFun: () -> Unit = {},
                                    errorFun: () -> Unit = {}) {
    Glide.with(this.context)
            .load(imageUri)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?,
                                             model: Any?,
                                             target: Target<Drawable>?,
                                             dataSource: DataSource?,
                                             isFirstResource: Boolean): Boolean {
                    successFun()
                    return false
                }

                override fun onLoadFailed(e: GlideException?,
                                          model: Any?,
                                          target: Target<Drawable>?,
                                          isFirstResource: Boolean): Boolean {
                    errorFun()
                    return false
                }
            })
            .into(this)
}
