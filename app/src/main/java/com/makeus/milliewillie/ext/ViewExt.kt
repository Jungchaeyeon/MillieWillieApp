package com.makeus.milliewillie.ext

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("lineTint")
fun View.lineTint(color: String) {
    (background as GradientDrawable).setStroke(3, Color.parseColor(color))
}
@BindingAdapter("BgTint")
fun View.BgTint(color: String) {
    (background as Drawable).setTint(Color.parseColor(color))
}
@BindingAdapter("lineTint")
fun View.lineTint(color: Int) {
    (background as GradientDrawable).setStroke(3, color)
}