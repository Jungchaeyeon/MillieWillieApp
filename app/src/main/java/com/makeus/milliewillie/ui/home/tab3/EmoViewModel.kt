package com.makeus.milliewillie.ui.home.tab3

import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.EmotionImg
import com.makeus.milliewillie.model.EmotionsRecordDayResponse
import com.makeus.milliewillie.model.EmotionsRecordRequest
import com.makeus.milliewillie.model.EmotionsRecordResponse
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers

class EmoViewModel(val apiRepository: ApiRepository, val  repositoryCached: RepositoryCached): BaseViewModel() {

    var emotionsRecordRequest = EmotionsRecordRequest()
    lateinit var emotionsRecordDayResponse : EmotionsRecordDayResponse
    lateinit var emotionsRecordResponse : EmotionsRecordResponse
    val liveEmoList = MutableLiveData<List<EmotionImg>>()
    val liveTodayData = MutableLiveData<String>()
    val livePickEmo= MutableLiveData<EmotionImg>()
    val liveEmoMemo = MutableLiveData<String>()
   // var emoDayKey = ArrayList<EmotionsRecordDayResponse.Result>()

    //POST EMO
    fun postEmotionsRecord()=
        apiRepository.postEmotionsRecord(
            EmotionsRecordRequest(
                content = emotionsRecordRequest.content,
                emotion= emotionsRecordRequest.emotion
            )
        )
            .subscribe {
            if(it.isSuccess){
                Log.e("감정 기록 생성 완료")
                emotionsRecordResponse = it
                repositoryCached.setValue(LocalKey.EMOTIONID, it.result.emotionRecordId)
            }
        }.disposeOnDestroy(this)

    //PATCH EMO
    fun patchEmotionsRecord()=
        apiRepository.patchEmotionsRecord(
            EmotionsRecordRequest(
                content = emotionsRecordRequest.content,
                emotion= emotionsRecordRequest.emotion
            ),
            path = repositoryCached.getEmotionId()
        ).subscribe {
            if(it.isSuccess){
                emotionsRecordResponse = it
                repositoryCached.setValue(LocalKey.EMOTIONID, it.result.emotionRecordId)
            }
        }.disposeOnDestroy(this)

    //DELETE EMO
    fun deleteEmotionsRecord(response: (Boolean) -> Unit)=
        apiRepository.deleteEmotionsRecord(
            path = 0L
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                response.invoke(true)
        },{
                response.invoke(false)
            }).disposeOnDestroy(this)
    //
    //GET EMO DAY
    fun getEmotionsRecordDay( response: (Boolean) -> Unit)=
        apiRepository.getEmotionsRecordDay(
            path ="2020-03-25"
        ).subscribe {
            if(it.isSuccess){
                Log.e("이모지 일별 조회 성공")
                emotionsRecordDayResponse = it
                response.invoke(true)
            }
            else{
                response.invoke(false)
            }
        }.disposeOnDestroy(this)

    //GET EMO MONTH
    fun getEmotionsRecordMonth( response: (Boolean) -> Unit)=
        apiRepository.getEmotionsRecordMonth(
            path ="2020-03-25"
        ).subscribe {
            if(it.isSuccess){
                Log.e("이모지 월별 조회 성공")
                emotionsRecordDayResponse = it
                response.invoke(true)
            }
            else{
                response.invoke(false)
            }
        }.disposeOnDestroy(this)
    fun requestEmo(){
        liveEmoList.postValue(listOf(
            EmotionImg(R.drawable.emo_1_happy,"행복해",1),
            EmotionImg(R.drawable.emo_2_sad,"슬퍼",2),
            EmotionImg(R.drawable.emo_3_angry,"화가나!",3),
            EmotionImg(R.drawable.emo_4_surprised,"힘들어..",4),
            EmotionImg(R.drawable.emo_5_gunchim,"gunchim",5),
            EmotionImg(R.drawable.emo_6_umm,"흠..",6),
            EmotionImg(R.drawable.emo_7_frustrated,"???",7),
            EmotionImg(R.drawable.emo_8_soso,"룰루~",8),
            EmotionImg(R.drawable.emo_9_satisfied,"좋-아",9)
        ))
    }
    fun nextEmo(id:Int): Int {
        when(id){
            R.drawable.emo_1_happy-> return R.drawable.emo_happy_right
            R.drawable.emo_2_sad-> return R.drawable.emo_sad_right
            R.drawable.emo_3_angry-> return R.drawable.emo_angry_right
            R.drawable.emo_4_surprised-> return R.drawable.emo_surprised_right
            R.drawable.emo_5_gunchim-> return R.drawable.emo_gunchim_right
            R.drawable.emo_6_umm-> return R.drawable.emo_umm_right
            R.drawable.emo_7_frustrated->return R.drawable.emo_frustrated_right
            R.drawable.emo_8_soso-> return R.drawable.emo_smug_right
            R.drawable.emo_9_satisfied-> return R.drawable.emo_soso_right
        }
        return 0
    }
}