package com.makeusteam.milliewillie.ext

import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView


@BindingAdapter("setAni")
fun LottieAnimationView.setAnimation(Ani: Int) {
    this.setAnimation(Ani)
}

