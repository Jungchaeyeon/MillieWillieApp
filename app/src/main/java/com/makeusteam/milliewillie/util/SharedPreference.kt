package com.makeusteam.milliewillie.util

import com.makeusteam.milliewillie.MyApplication.Companion.MILLI_WILLI
import com.makeusteam.milliewillie.MyApplication.Companion.sSharedPreferences
import org.json.JSONArray
import org.json.JSONException

object SharedPreference {
    fun putSettingItem(key: String, value: String?) {
        Log.e("Put $key (value : $value ) to $MILLI_WILLI")
        val editor = sSharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getSettingItem(key: String): String? {
        Log.e("Return ${sSharedPreferences.getString(key, "0")}")
        return sSharedPreferences.getString(key, "0")
    }

    fun putSettingBooleanItem(key: String, value: Boolean) {
        Log.e("Put $key (value : $value ) to $MILLI_WILLI")
        val editor = sSharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getSettingBooleanItem(key: String): Boolean {
        Log.e("Return ${sSharedPreferences.getBoolean(key, false)}")
        return sSharedPreferences.getBoolean(key, false)
    }

    fun putArrayStringItem(key: String, value: ArrayList<String>?) {
        val editor = sSharedPreferences.edit()

        val jsonList = JSONArray()
        if (value != null) {
            for (i in 0 until value.size) {
                jsonList.put(value[i])
            }
        }
        if (value != null) {
            if (value.isNotEmpty()) {
                editor.putString(key, jsonList.toString())
            } else {
                editor.putString(key, "")
            }
        }
        editor.apply()
        Log.e("Put $key (value : $value ) to $MILLI_WILLI")
        Log.e("result $key (value : $jsonList ) to $MILLI_WILLI")
    }

    fun getArrayStringItem(key: String): ArrayList<String> {
        Log.e("Get $key from $MILLI_WILLI")
        val json = sSharedPreferences.getString(key, "")
        val returnList = ArrayList<String>()
        if (json != null) {
            try {
                val jsonList = JSONArray(json)
                for (i in 0 until jsonList.length()) {
                    val url = jsonList.optString(i)
                    returnList.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        Log.e("Return $returnList")
        return returnList
    }
}