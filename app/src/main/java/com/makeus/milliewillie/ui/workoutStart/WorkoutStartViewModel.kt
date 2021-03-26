package com.makeus.milliewillie.ui.workoutStart

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.StartRecyclerCircleItem
import com.makeus.milliewillie.model.StartRecyclerItem
import com.makeus.milliewillie.util.Log

class WorkoutStartViewModel: BaseViewModel() {

    val liveDataItemList = MutableLiveData<ArrayList<StartRecyclerItem>>()
    val _itemList = ArrayList<StartRecyclerItem>()

    val liveDataCircleItemList = MutableLiveData<ArrayList<StartRecyclerCircleItem>>()
    val _circleItemList = ArrayList<StartRecyclerCircleItem>()

    val liveDataTimeSec = MutableLiveData<String>().apply { value = "00" }
    val liveDataTimeMin = MutableLiveData<String>().apply { value = "00" }
    val liveDataTimeHour = MutableLiveData<String>().apply { value = "00" }

    init {
        defaultItemList()
        defaultCircleItemList()
    }

    fun defaultItemList() {
        createItemList()
        liveDataItemList.value = _itemList

        Log.e("liveDataItemList ${liveDataItemList.value}")
    }

    fun createItemList() {
        for (i in 0..9) {
            _itemList.add(StartRecyclerItem(exName = "밀리터리 프레스", exInfo = "5set, 10kg, 5개"))
        }
    }

    fun onClickItemList(position: Int) {
//        _circleItemList.removeAt(position)
//        _circleItemList.add(position, StartRecyclerCircleItem(R.drawable.circle_recycler_item_select))
    }

    fun defaultCircleItemList() {
        for (i in 0..4) {
            _circleItemList.add(StartRecyclerCircleItem())
        }
        liveDataCircleItemList.value = _circleItemList
        Log.e("liveDataCircleItemList ${liveDataCircleItemList.value}")
//        liveDataCircleItemList.postValue(_circleItemList)
//        Log.e(liveDataCircleItemList.value.toString())
//        Log.e(_circleItemList.toString())

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


}