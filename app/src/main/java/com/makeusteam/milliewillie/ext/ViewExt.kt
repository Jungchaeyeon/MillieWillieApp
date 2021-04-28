package com.makeusteam.milliewillie.ext

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.Guideline
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
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
@BindingAdapter("setImageUrl")
fun View.setImageUrl(value: String){
    (imageView as ImageView).setImageUrl(value)
}
