package com.makeus.milliewillie.ui.home.tab3

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.EmotionImg

class EmoViewModel: BaseViewModel() {
    val liveEmoList = MutableLiveData<List<EmotionImg>>()
    val liveTodayData = MutableLiveData<String>()
    val livePickEmo= MutableLiveData<EmotionImg>()
    val liveEmoMemo = MutableLiveData<String>()

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