package com.makeus.milliewillie.ui.intro

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.Intro

class WelcomeViewModel : BaseViewModel() {
    val introItemList = MutableLiveData<List<Intro>>()

    init{
        requestIntroItemList()
    }
    fun requestIntroItemList() {
        introItemList.postValue(
            listOf(

                Intro(
                    R.drawable.mini_oval_aquablue, R.drawable.mini_oval_gray, "안녕하세요!\n" +
                            "만나뵙게되어 반가워요 ;)", R.raw.ani_welcome
                ),
                Intro(
                    R.drawable.mini_oval_gray, R.drawable.mini_oval_aquablue, "밀리윌리가\n" +
                            "국군 장병들을 위해\n" +
                            "작은 서비스를 준비했어요.", R.raw.ani_splash
                )

            )
        )
    }

}