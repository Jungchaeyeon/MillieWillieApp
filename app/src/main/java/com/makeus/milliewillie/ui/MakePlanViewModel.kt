package com.makeus.milliewillie.ui

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.Plan
import petrov.kristiyan.colorpicker.ColorPicker

class MakePlanViewModel : BaseViewModel() {


    val livePlanTypeList =MutableLiveData<List<String>>()
    val livePlanType = MutableLiveData<String>()
    val livePlanTodoList = MutableLiveData<List<Plan.Todos>>()

    fun requestPlanTypeList(){
        livePlanTypeList.postValue(listOf(
            "일정","정기 휴가","포상 휴가",
           "외박","훈련","면회",
           "외출","전투휴무","당직"
        ))
    }
    fun requestPlanTodoList(){
        livePlanTodoList.postValue(listOf(
            Plan.Todos(),
            Plan.Todos("나갈 때 닥터지"),
            Plan.Todos(" 챙길물품, 확인해야할 사항 기록!")
        ) )
    }

    val planType: LiveData<String>
      get() = livePlanType

    val getPlanType:  MutableLiveData<String>
        get() = livePlanType

    fun openColorPicker(context: Context,response : (Boolean) -> Unit) {
        val colorPicker = ColorPicker(context as Activity?)
        val colors = arrayListOf<String>()

        colors.add("#ffbe65")//bright_yellow
        colors.add("#ff91bc")//pink
        colors.add("#5df2ad")//light_greenish_blue
        colors.add("#15eff7")//bright_aqua
        colors.add("#6b8fc9")//faded blue
        colors.add("#5093ff")//dodger blue
        colors.add("#8a6fff")//periwinkle
        colors.add("#dfe5e6")//silver
        colors.add("#b2babf")//cool_gray
        colors.add("#7e8b90")//steel_gray

        colorPicker.negativeButton.text=R.string.cancel.toString()
        colorPicker.setColors(colors)
            .setTitle("일정 색상")
            .setColumns(5)
            .setRoundColorButton(true)
            .disableDefaultButtons(true)
            .addListenerButton("확인", object : ColorPicker.OnButtonListener{
                override fun onClick(v: View?, position: Int, color: Int) {
                    response.invoke(true)
                    colorPicker.dismissDialog()
                }

            })
            .setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {
                    //
                }

                override fun onCancel() {
                    response.invoke(false)
                }
            }).show()

    }

}




