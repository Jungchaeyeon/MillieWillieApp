package com.makeus.milliewillie.ui

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.MainSchedule

class MainViewModel : BaseViewModel() {
    val liveMainScheduleList = MutableLiveData<List<MainSchedule>>()

    //val swipeHelperCallback = SwipeHelperCallback()
    //val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)

    fun requestMainScheduleData() {
        liveMainScheduleList.postValue(listOf(
            MainSchedule(),
            MainSchedule("적금 확인"),
            MainSchedule("첫 휴우가"),
            MainSchedule("스크롤")

        ))
    }

}