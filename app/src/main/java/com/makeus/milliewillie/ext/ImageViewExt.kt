package com.makeus.milliewillie.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("setImage")
fun ImageView.setImage(Image: Int) {
    this.setImageResource(Image)
}


@BindingAdapter("loadUrl")
fun ImageView.loadUrl(url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }

    Glide.with(this).load(url).into(this)
}