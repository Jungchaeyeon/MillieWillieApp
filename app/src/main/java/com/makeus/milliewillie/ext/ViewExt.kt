package com.makeus.milliewillie.ext

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.Guideline
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.makeus.milliewillie.R
import kotlinx.android.synthetic.main.activity_intro_goal.view.*
import kotlinx.android.synthetic.main.item_home_layout.view.*

@BindingAdapter("lineTint")
fun View.lineTint(color: String) {
    (background as GradientDrawable).setStroke(3, Color.parseColor(color))
}
@BindingAdapter("bgTint")
fun View.bgTint(color: String) {
    (background as GradientDrawable).setTint(Color.parseColor(color))
}
@BindingAdapter("lineTint")
fun View.lineTint(color: Int) {
    (background as GradientDrawable).setStroke(3, color)
}
@BindingAdapter("setPercent")
fun View.setPercent(value: Float) {
    (guideline as Guideline).setGuidelinePercent(value)
}
@BindingAdapter("setProgress")
fun View.setProgress(value:MutableLiveData<String>){
    (progressbar as ProgressBar).progress = value.value?.toInt()!!
}

//@BindingAdapter("setImageGlide")
//fun View.setImageGlide(value: String) {
//    Glide.with((imageView as ImageView))
//        .load(value)
//        .circleCrop()
//        .placeholder(R.drawable.graphic_profile_big)
//        .into((imageView as ImageView))
//}