package com.momentolabs.app.security.applocker.util.binding

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.momentolabs.app.security.applocker.R
import com.squareup.picasso.Picasso
import java.io.File

@BindingAdapter("srcCompat")
fun setVectorDrawable(imageView: AppCompatImageView, drawable: Drawable?) {
    drawable?.let { imageView.setImageDrawable(drawable) }
}

@BindingAdapter("url")
fun loadUrl(imageView: AppCompatImageView, url: String) {
    if (url.isNotEmpty()) {

        Picasso.Builder(imageView.context).build()
            .load(url)
            .placeholder(R.drawable.placeholder)
            .into(imageView)
    }
}

@BindingAdapter(value = ["file", "imageSize"], requireAll = false)
fun loadFile(imageView: AppCompatImageView, url: String, imageSize: ImageSize?) {
    if (url.isNotEmpty()) {
        val picassoRequestCreator = Picasso.Builder(imageView.context).build()
            .load(File(url))
            .placeholder(R.drawable.placeholder)

        if (imageSize != null && imageSize != ImageSize.ORIGINAL) {
            picassoRequestCreator
                .resize(0, imageSize.size.toInt())
        }

        picassoRequestCreator.into(imageView)

    }
}