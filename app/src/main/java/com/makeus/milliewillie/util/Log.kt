package com.makeus.milliewillie.util

import android.util.Log
import com.makeus.milliewillie.BuildConfig

object Log {
    fun d(log: String?, tag: String = "makeUs") {
        if (BuildConfig.DEBUG) {
            Log.d(tag, log ?: "string is null")
        }
    }

    fun e(log: String?, tag: String = "makeUs") {
        if (BuildConfig.DEBUG) {
            Log.e(tag, log ?: "string is null")
        }
    }
}