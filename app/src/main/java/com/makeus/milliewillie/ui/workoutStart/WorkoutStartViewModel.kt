package com.makeus.milliewillie.ui.workoutStart

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.StartRecyclerCircleItem
import com.makeus.milliewillie.model.StartRecyclerItem

class WorkoutStartViewModel: BaseViewModel() {

    val liveDataItemList = MutableLiveData<ArrayList<StartRecyclerItem>>()
    val _itemList = ArrayList<StartRecyclerItem>()

    val liveDataCircleItemList = MutableLiveData<ArrayList<StartRecyclerCircleItem>>()
    val _circleItemList = ArrayList<StartRecyclerCircleItem>()

    init {
        defaultItemList()
        defaultCircleItemList()
    }

    fun defaultItemList() {
        createItemList()
        liveDataItemList.postValue(_itemList)
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
            _circleItemList.add(StartRecyclerCircleItem(R.drawable.circle_recycler_item_unselect))
        }
        liveDataCircleItemList.postValue(_circleItemList)
    }




}