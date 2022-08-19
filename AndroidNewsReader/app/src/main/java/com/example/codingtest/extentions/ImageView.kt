package com.example.codingtest.extentions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.codingtest.R

internal fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).thumbnail(0.1f)
        .placeholder(R.drawable.ic_broken_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.ic_broken_image).dontAnimate().into(this)
}