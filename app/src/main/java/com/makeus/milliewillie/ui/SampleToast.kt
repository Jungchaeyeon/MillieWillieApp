package com.makeus.milliewillie.ui

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.LayoutToastSimpleBinding
import com.makeus.milliewillie.databinding.LayoutToastSimpleNonCheckBinding

object SampleToast {

//    fun createToast(context: Context, message: String): Toast? {
//        val inflater = LayoutInflater.from(context)
//        val binding: LayoutToastSimpleBinding =
//            DataBindingUtil.inflate(inflater, R.layout.layout_toast_simple, null, false)
//
//        binding.tvSample.text = message
//
//        return Toast(context).apply {
//            setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 36.toPx())
//            duration = Toast.LENGTH_LONG
//            view = binding.root
//        }
//    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun createToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: LayoutToastSimpleNonCheckBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_toast_simple_non_check, null, false)

        binding.nonTvSample.text = message

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 36.toPx())
            duration = Toast.LENGTH_LONG
            view = binding.root
        }
    }
}