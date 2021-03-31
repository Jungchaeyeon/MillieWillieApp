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

    fun getExerciseId() = getValue(LocalKey.EXERCISEID , 0).toLong()
    fun getIsExerciseId() = getValue(LocalKey.ISEXERCISEID , false)
    fun getIsInputWeight() = getValue(LocalKey.ISINPUTWEIGHT , false)
    fun getIsInputGoal() = getValue(LocalKey.ISINPUTGOAL , false)
    fun getPostYear() = getValue(LocalKey.POSTYEAR , 10000)
    fun getPostMonth() = getValue(LocalKey.POSTMONTH , 100)
    fun getPostDay() = getValue(LocalKey.POSTDAY , 100)
    fun getWeightTime() = getGeneralValue(LocalKey.WEIGHTTIME , "")
    fun getLogout() = getGeneralValue(LocalKey.LOGOUT , false)
    fun getLoginType() = getGeneralValue(LocalKey.LOGINTYPE , "")
    fun getToken() = getGeneralValue(LocalKey.TOKEN , "없음")
    fun getInApp() = getGeneralValue(LocalKey.INAPP , "T")
    fun getPickDate() = getGeneralValue(LocalKey.PICKDATE , "날짜선택")
    fun getType() = getValue(LocalKey.TYPE, "")
    fun getTypeDetail() = getValue(LocalKey.DETAILTYPE, "")
    fun getDate() = getValue(LocalKey.DATE, "")
    fun getPlanType() = getValue(LocalKey.PLANTYPE, "일정")
    fun getColor() = getValue(LocalKey.COLOR, "#ffbe65")
    fun getOnlyDay() = getValue(LocalKey.ONLYDAY, "")
    fun getDayNight() = getValue(LocalKey.DAYNIGHT, "")
    fun getStartDate() = getGeneralValue(LocalKey.STARTDATE, "")
    fun getEnd() = getGeneralValue(LocalKey.ENDDATE, "")
    fun getPlanStartDate() = getGeneralValue(LocalKey.PLANSTART,"")
    fun getPlanEndDate() = getGeneralValue(LocalKey.PLANEND,"")
    fun getGoal() = getValue(LocalKey.GOAL, "파이팅")
    fun getMiliDday() = getValue(LocalKey.MILIDDAY, "")
    fun getDDay() = getValue(LocalKey.ENDDDAY, "")
    fun getnextDDay() = getValue(LocalKey.NEXTDDAY, "")
    fun getmonthDDay() = getValue(LocalKey.MONTHDDAY, "")
    fun getIsMember() = getValue(LocalKey.ISMEMBER, false)
    fun getSocialType() = getValue(LocalKey.SOCIALTYPE, "")
    fun getholiExist() = getValue(LocalKey.HOLIEXIST, false)
    fun getAvailHoli() = getValue(LocalKey.AVAILHOLI, 0)
    fun getDday() = getValue(LocalKey.ALLDDAY, 0)//percent
    fun getNextPromDday() = getValue(LocalKey.NEXTPROMDDAY, 0)//percent
    fun getMonthPromDday() = getValue(LocalKey.MONTHPROMDDAY, 0)//percent
    fun getPlanTotalDay() = getValue(LocalKey.MONTHPROMDDAY, "")//percent
    fun getVacaId() = getValue(LocalKey.PATCHVACID, "")
    fun getDiaryId() = getValue(LocalKey.DIARYID, "")
    fun getPlanId() = getGeneralValue(LocalKey.PLANID, "0")
    fun getVac1Id() = getGeneralValue(LocalKey.VAC1ID, "0")
    fun getVac2Id() = getGeneralValue(LocalKey.VAC2ID, "0")
    fun getVac3Id() = getGeneralValue(LocalKey.VAC3ID, "0")
    fun getEmotionId() = getGeneralValue(LocalKey.EMOTIONID, 0L)
    fun getWorkId() = getGeneralValue(LocalKey.WORKID, "0")
    fun getFirstAccess() = getGeneralValue(LocalKey.FIRSTACCESS, "T")


    protected abstract fun setRawValue(key: LocalKey, value: Any?)
    protected abstract fun getRawValue(key: LocalKey): String?
    protected abstract fun clear()
}