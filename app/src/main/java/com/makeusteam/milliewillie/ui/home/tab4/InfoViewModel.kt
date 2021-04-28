package com.makeusteam.milliewillie.ui.home.tab4

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.MyApplication.Companion.userName
import com.makeusteam.milliewillie.MyApplication.Companion.userProfileImgUrl
import com.makeusteam.milliewillie.repository.ApiRepository

class InfoViewModel(val apiRepository: ApiRepository): BaseViewModel() {

    val liveDataUserName = MutableLiveData<String>().apply { value = userName }
    val liveDataUserProfileUrl = MutableLiveData<String>().apply { value = userProfileImgUrl }




}