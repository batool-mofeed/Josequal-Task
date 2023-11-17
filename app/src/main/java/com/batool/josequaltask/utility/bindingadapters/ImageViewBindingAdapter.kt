package com.batool.josequaltask.utility.bindingadapters

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load

/**
 * Created By Batool Mofeed - Vibes Solutions on 11/18/2023.
 **/
object ImageViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("loadImage")
    fun AppCompatImageView.loadImage(imageUrl: String?) {
        if (imageUrl != null) {
            if (imageUrl.isNotEmpty()) {
                load(imageUrl)
            }
        } else {
            invalidate()
        }
    }
}