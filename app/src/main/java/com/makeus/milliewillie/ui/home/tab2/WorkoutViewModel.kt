package com.makeus.milliewillie.ui.home.tab2

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.DdayCheckList
import com.makeus.milliewillie.model.TodayExDayOfWeek

class WorkoutViewModel: BaseViewModel() {
    val todayExItemList = MutableLiveData<ArrayList<TodayExDayOfWeek>>()

    val todayExArrayList = arrayListOf<TodayExDayOfWeek>(TodayExDayOfWeek("일"),TodayExDayOfWeek("월"),
        TodayExDayOfWeek("화"), TodayExDayOfWeek("수"), TodayExDayOfWeek("목")
        ,TodayExDayOfWeek("목"), TodayExDayOfWeek("금"), TodayExDayOfWeek("오늘"))

    init{
        defaultTodayExItemList()
    }
    fun defaultTodayExItemList() {
        todayExItemList.postValue(todayExArrayList)
    }

    fun addItem(item: TodayExDayOfWeek) {
        todayExArrayList.add(TodayExDayOfWeek(item.day))
        todayExItemList.value=todayExArrayList
        android.util.Log.e("makeUs", "checkArrayList: $todayExArrayList")
    }

    fun removeItem(position: Int) {
        todayExArrayList.removeAt(position)
        todayExItemList.value = todayExArrayList
        android.util.Log.e("makeUs", "checkArrayList: $todayExArrayList")
    }

}