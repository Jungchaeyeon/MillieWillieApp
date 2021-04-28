package com.makeusteam.milliewillie.ui.workoutStart

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.model.StartRecyclerCircleItem
import com.makeusteam.milliewillie.model.StartRecyclerItem
import com.makeusteam.milliewillie.repository.ApiRepository
import com.makeusteam.milliewillie.util.Log

class WorkoutStartViewModel(val apiRepository: ApiRepository): BaseViewModel() {

    var liveDataItemList = MutableLiveData<ArrayList<StartRecyclerItem>>()
    val _itemList = ArrayList<StartRecyclerItem>()

    var liveDataCircleItemList = MutableLiveData<ArrayList<StartRecyclerCircleItem>>()
    val _circleItemList = ArrayList<StartRecyclerCircleItem>()

    val liveDataTimeSec = MutableLiveData<String>().apply { value = "00" }
    val liveDataTimeMin = MutableLiveData<String>().apply { value = "00" }
    val liveDataTimeHour = MutableLiveData<String>().apply { value = "00" }

    init {
//        defaultItemList()
//        defaultCircleItemList()
    }


    fun increaseSec(value: String) {
        liveDataTimeSec.postValue("${value.toInt() + 1}")
    }
    fun increaseMin(value: String) {
        liveDataTimeMin.postValue("${value.toInt() + 1}")
    }
    fun increaseHour(value: String) {
        liveDataTimeHour.postValue("${value.toInt() + 1}")
    }

    fun defaultItemList() {
        liveDataItemList.postValue(_itemList)

        Log.e("liveDataItemList ${liveDataItemList.value}")
    }

    fun countCompleteSet(circleList: ArrayList<StartRecyclerCircleItem>): Int {
        var count = 0
        for (i in 0 until circleList.size) {
            if (circleList[i].circle == R.drawable.one_currnet_blue) {
                count++
            }
        }
        return count
    }



}