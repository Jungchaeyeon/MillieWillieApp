package com.makeus.milliewillie.ui

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.ServiceDetailType
import com.makeus.milliewillie.ui.fragment.DatePickerBasicBottomSheetDialogFragment
import java.util.*

class UserViewModel : BaseViewModel(){
  //  val liveuserData = MutableLiveData<User>()

    val liveTypeDetailSoldier = MutableLiveData<List<ServiceDetailType>>()
    val customDialog1 = DatePickerBasicBottomSheetDialogFragment.getInstance()
    val customDialog2 = DatePickerBasicBottomSheetDialogFragment.getInstance()
    val customDialog3 = DatePickerBasicBottomSheetDialogFragment.getInstance()
    val customDialog4 = DatePickerBasicBottomSheetDialogFragment.getInstance()
    val customDialog5 = DatePickerBasicBottomSheetDialogFragment.getInstance()
    val customDialog6 = DatePickerBasicBottomSheetDialogFragment.getInstance()

    val liveBtnEnlist = MutableLiveData<String>()
    val liveBtnDischarge = MutableLiveData<String>()
    val liveBtnPrivate = MutableLiveData<String>()
    val liveBtnCorporal = MutableLiveData<String>()
    val liveBtnSergeant= MutableLiveData<String>()
    val liveBtnPromotion= MutableLiveData<String>()

    init {
        liveBtnEnlist.postValue(today())
        liveBtnPrivate.postValue(today())
        liveBtnCorporal.postValue(today())
        liveBtnSergeant.postValue(today())

    }

    fun today(): String{
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR).toString() +"." + (today.get(Calendar.MONTH) + 1).toString() + "."+ today.get(Calendar.DAY_OF_MONTH).toString()
    }


    fun requestDetailSoldier(){
        liveTypeDetailSoldier.postValue(listOf(
            ServiceDetailType("육군"), ServiceDetailType("해군"), ServiceDetailType("공군"),
            ServiceDetailType("해병대"), ServiceDetailType("의경"), ServiceDetailType("카투사"),
            ServiceDetailType("특전사"), ServiceDetailType("의무소방"),ServiceDetailType("해양의무경찰"),
            ServiceDetailType("사회복무요원"), ServiceDetailType("산업기능요원","보충역"),ServiceDetailType("산업기능요원","현역"),
            ServiceDetailType("전문연구요원")
        ))
    }
    fun requestDetailSergeant(){
        liveTypeDetailSoldier.postValue(listOf(
            ServiceDetailType("기술행정"), ServiceDetailType("회전익 항공기"), ServiceDetailType("항공운항"),
            ServiceDetailType("통/번역"), ServiceDetailType("특전")
        ))
    }
    fun requestDetailCaptain(){
        liveTypeDetailSoldier.postValue(listOf(
            ServiceDetailType("육군"), ServiceDetailType("해군"), ServiceDetailType("공군"),
            ServiceDetailType("해병대")
        ))
    }

}