package com.makeus.milliewillie.ui.home.tab4

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.MyApplication.Companion.userName
import com.makeus.milliewillie.MyApplication.Companion.userProfileImgUrl
import com.makeus.milliewillie.repository.ApiRepository

class InfoViewModel(val apiRepository: ApiRepository): BaseViewModel() {

    val liveDataUserName = MutableLiveData<String>().apply { value = userName }
    val liveDataUserProfileUrl = MutableLiveData<String>().apply { value = userProfileImgUrl }




}