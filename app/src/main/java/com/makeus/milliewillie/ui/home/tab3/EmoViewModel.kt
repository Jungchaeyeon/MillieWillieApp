package com.makeus.milliewillie.ui.home.tab3

import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

class EmoViewModel(val apiRepository: ApiRepository, val repositoryCached: RepositoryCached) :
    BaseViewModel() {
    val emotionsRecordMonthData = MutableLiveData<EmotionsRecordMonthResponse>()
    var emotionOnlyMonthData = ArrayList<EmotionRecordOnlyMonthResponse.Result>()
    var emotionMonthData = EmotionsRecordMonthResponse().result.month
    var emotionMonthToday =EmotionsRecordMonthResponse().result.today
    var emotionsRecordRequest = EmotionsRecordRequest()
    var emotionsRecordResponse = EmotionsRecordResponse()
    val liveEmoList = MutableLiveData<List<EmotionImg>>()
    val liveTodayData = MutableLiveData<String>()
    val livePickEmo = MutableLiveData<EmotionImg>()
    val liveEmoMemo = MutableLiveData<String>()
    val calList = List<Calendar>(31){ Calendar.getInstance()}
    var monthEmoSize = 0
    val cal = Calendar.getInstance()
    val df = SimpleDateFormat("yyyyMM")
    var month = df.format(cal.time)
    // var emoDayKey = ArrayList<EmotionsRecordDayResponse.Result>()


    //POST EMO
    fun postEmotionsRecord(response: (Boolean) -> Unit) =
        apiRepository.postEmotionsRecord(
            EmotionsRecordRequest(
                date = emotionsRecordRequest.date,
                content = emotionsRecordRequest.content,
                emotion = emotionsRecordRequest.emotion
            )
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("감정 기록 생성 완료")
                    response.invoke(true)
                    emotionsRecordResponse = it
                    repositoryCached.setValue(LocalKey.EMOTIONID, it.result.emotionRecordId)
                } else {
                    emotionsRecordResponse = it
                    Log.e("감정 기록 생성 실패")
                    response.invoke(false)
                }
            }.disposeOnDestroy(this)

    //PATCH EMO
    fun patchEmotionsRecord(response: (Boolean) -> Unit) =
        apiRepository.patchEmotionsRecord(
            EmotionsRecordRequest(
                content = emotionsRecordRequest.content,
                emotion = emotionsRecordResponse.result.emotion
            ),
            path = emotionsRecordResponse.result.emotionRecordId
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    emotionsRecordResponse = it
                    repositoryCached.setValue(LocalKey.EMOTIONID, it.result.emotionRecordId)
                    response.invoke(true)
                } else {
                    response.invoke(false)
                }
            }.disposeOnDestroy(this)

    //DELETE EMO
    fun deleteEmotionsRecord(response: (Boolean) -> Unit) =
        apiRepository.deleteEmotionsRecord(
            path = emotionsRecordResponse.result.emotionRecordId
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                response.invoke(true)
            }, {
                response.invoke(false)
            }).disposeOnDestroy(this)

    //
    //GET EMO DAY
    fun getEmotionsRecordDay(response: (Boolean) -> Unit) =
        apiRepository.getEmotionsRecordDay(
            date = repositoryCached.getPickDay()
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("이모지 일별 기록 있음")
                    emotionsRecordResponse = it

                    liveEmoMemo.value = it.result.content
                    Log.e(it.result.emotion.toString(),"이모션 id")
                    Log.e(it.result.emotionText.toString(),"이모션 text")

                    response.invoke(true)
                } else {
                    Log.e("이모지 일별 기록 없음")
                    emotionsRecordResponse = it
                    response.invoke(false)
                }
            }.disposeOnDestroy(this)

    //GET EMO MONTH
    fun getEmotionsRecordMonth(response: (Boolean) -> Unit)=
        apiRepository.getEmotionsRecordMonth(
            month = month
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                emotionOnlyMonthData = it.result as ArrayList<EmotionRecordOnlyMonthResponse.Result>

                Log.e(emotionOnlyMonthData.toString(),"EMOTIONMONTHDATA")

                monthEmoSize = emotionOnlyMonthData.size
                Log.e(monthEmoSize.toString())
                response.invoke(true)
            }.disposeOnDestroy(this)

    fun getEmotionsFirstMonth(response: (Boolean) -> Unit)=
        apiRepository.getEmotionsFirstMonth()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                emotionMonthData = it.result.month
                emotionMonthToday = it.result.today

                Log.e(emotionMonthData.toString(),"EMOTIONMONTHDATA")
                Log.e(emotionMonthToday.toString(),"EMOTIONMONTHTODAY")

                monthEmoSize = emotionMonthData?.size!!
                Log.e(monthEmoSize.toString())
                response.invoke(true)
            }.disposeOnDestroy(this)

//            : Observable<EmotionsRecordMonthResponse>
//            .doOnNext {
//            liveEmotionsRecordMonthData.postValue(it)
//        }

    fun requestEmo() {
        liveEmoList.postValue(listOf(
            EmotionImg(R.drawable.emo_1_happy, "행복해", 1),
            EmotionImg(R.drawable.emo_2_sad, "슬퍼", 2),
            EmotionImg(R.drawable.emo_3_angry, "화가나!", 3),
            EmotionImg(R.drawable.emo_4_surprised, "힘들어..", 4),
            EmotionImg(R.drawable.emo_5_gunchim, "gunchim", 5),
            EmotionImg(R.drawable.emo_6_umm, "흠..", 6),
            EmotionImg(R.drawable.emo_7_frustrated, "???", 7),
            EmotionImg(R.drawable.emo_8_soso, "룰루~", 8),
            EmotionImg(R.drawable.emo_9_satisfied, "좋-아", 9)
        ))
    }

    fun nextEmo(id: Int): Int {
        when (id) {
            1 -> return R.drawable.emo_happy_right
            2 -> return R.drawable.emo_sad_right
            3 -> return R.drawable.emo_angry_right
            4 -> return R.drawable.emo_surprised_right
            5 -> return R.drawable.emo_gunchim_right
            6 -> return R.drawable.emo_umm_right
            7 -> return R.drawable.emo_frustrated_right
            8 -> return R.drawable.emo_smug_right
            9 -> return R.drawable.emo_soso_right
            else -> return 0
        }
    }
}
