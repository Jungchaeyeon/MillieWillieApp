package com.makeusteam.milliewillie.ui.profile

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.MyApplication
import com.makeusteam.milliewillie.repository.ApiRepository

class ProfileViewModel(val apiRepository: ApiRepository): BaseViewModel() {

    val liveDataUserName = MutableLiveData<String>().apply { value = MyApplication.userName }
    val liveDataUserBirth = MutableLiveData<String?>().apply { value = null }


}