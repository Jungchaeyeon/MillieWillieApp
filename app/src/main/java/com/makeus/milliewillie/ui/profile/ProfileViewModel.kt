package com.makeus.milliewillie.ui.profile

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.repository.ApiRepository

class ProfileViewModel(val apiRepository: ApiRepository): BaseViewModel() {

    val liveDataUserName = MutableLiveData<String>().apply { value = MyApplication.userName }
    val liveDataUserBirth = MutableLiveData<String?>().apply { value = null }


}