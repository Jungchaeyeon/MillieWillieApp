package com.makeusteam.milliewillie.ext

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SetTextI18n")
@BindingAdapter("setdday")
fun TextView.setdday(totalDay: String?) {
    totalDay?.let {
        this.text = "D - $totalDay"
    }

}
@SuppressLint("SetTextI18n")
@BindingAdapter("setHobongTitle")
fun TextView.setHobongTitle(hobong: String?) {
    hobong?.let {
        this.text = "$hobong 호봉 진급"
    }
}
@SuppressLint("SetTextI18n")
@BindingAdapter("setPromotionTitle")
fun TextView.setPromotionTitle(nextLevel: String?) {
    nextLevel?.let {
        this.text = "$nextLevel 진급"
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("setDateFormat")
fun TextView.setDateFormat(inputDate: String) {
    val dfParse = SimpleDateFormat("yyyy-MM-dd")
    val dfFormat = SimpleDateFormat("yyyy.MM.dd")

    if(dfParse.parse(inputDate)!=null){
        this.text =  dfFormat.format(dfParse.parse(inputDate)!!)
    }
}

@BindingAdapter("floatToString")
fun TextView.floatToString(inputDate: Float?) {
    inputDate?.let {
        this.text = inputDate.toString()
    }
}
