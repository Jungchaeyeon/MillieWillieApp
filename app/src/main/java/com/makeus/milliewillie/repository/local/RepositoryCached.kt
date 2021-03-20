package com.makeus.milliewillie.repository.local

import com.makeus.milliewillie.util.GsonFactory
import java.lang.reflect.Type

/***
 * @see setRawValue(key: LocalKey, value: Any?)
 * value : Type is Any, Buy it must saved String to Local
 * @see getRawValue(key: LocalKey)
 * Return : it must be String type, Boolean is false or true String
 ***/
abstract class RepositoryCached {
    private val hashCached = HashMap<String, String?>()
    private val gson = GsonFactory.get()

    private fun <T : Any> getValue(key: LocalKey, defValue: T): T {
        return gson.fromJson(getCachedValue(key), defValue::class.java as Type) ?: defValue
    }

    private fun <T : Any> getGeneralValue(key: LocalKey, defValue: T): T {
        return (getCachedValue(key) ?: defValue) as T
    }

    fun setValue(key: LocalKey, value: Any?) {
        hashCached[key.name] = value?.toString()
        setRawValue(key, value)
    }

    fun isEqual(key: LocalKey, target: String): Boolean {
        return getRawValue(key) == target
    }

    private fun getCachedValue(key: LocalKey): String? {
        if (hashCached.containsKey(key.name)) {
            return hashCached[key.name]
        }

        val value = getRawValue(key)
        hashCached[key.name] = value
        return value
    }

    fun getToken() = getGeneralValue(LocalKey.TOKEN , "")
    fun getName() = getValue(LocalKey.NAME , "")
    fun getType() = getValue(LocalKey.TYPE, "")
    fun getTypeDetail() = getValue(LocalKey.DETAILTYPE, "")
    fun getDate() = getValue(LocalKey.DATE, "")
    fun getPlanType() = getValue(LocalKey.PLANTYPE, "일정")
    fun getNotice() = getValue(LocalKey.PLANNOTICE, "")
    fun getColor() = getValue(LocalKey.COLOR, "#ffbe65")
    fun getDayNight() = getValue(LocalKey.DAYNIGHT, "")
    fun getStartDate() = getValue(LocalKey.STARTDATE, "")
    fun getEnd() = getValue(LocalKey.ENDDATE, "")
    fun getGoal() = getValue(LocalKey.GOAL, "파이팅")
    fun getMiliDday() = getValue(LocalKey.MILIDDAY, "")
    fun getDDay() = getValue(LocalKey.ENDDDAY, "")
    fun getnextDDay() = getValue(LocalKey.NEXTDDAY, "")
    fun getmonthDDay() = getValue(LocalKey.MONTHDDAY, "")

    protected abstract fun setRawValue(key: LocalKey, value: Any?)
    protected abstract fun getRawValue(key: LocalKey): String?
    protected abstract fun clear()
}