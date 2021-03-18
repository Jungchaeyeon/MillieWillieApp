package com.makeus.milliewillie.ui.routine

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.datatransport.cct.internal.LogEvent
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityMakeRoutineBinding
import com.makeus.milliewillie.ui.dDay.Classification
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel

class MakeRoutineActivity: BaseDataBindingActivity<ActivityMakeRoutineBinding>(R.layout.activity_make_routine) {

    private val viewModel by viewModel<MakeRoutineViewModel>()

    lateinit var everyDay: View
    lateinit var monday: View
    lateinit var tuesday: View
    lateinit var wendesday: View
    lateinit var thursday: View
    lateinit var friday: View
    lateinit var saturday: View
    lateinit var sunday: View


    override fun ActivityMakeRoutineBinding.onBind() {
        vi = this@MakeRoutineActivity
        vm = viewModel
        viewModel.bindLifecycle(this@MakeRoutineActivity)

        everyDay = binding.routineTextEveryday
        monday = binding.routineTextMonday
        tuesday = binding.routineTextTuesday
        wendesday = binding.routineTextWednesday
        thursday = binding.routineTextThursday
        friday = binding.routineTextFriday
        saturday = binding.routineTextSaturday
        sunday = binding.routineTextSunday

        everyDay.isSelected = true


    }

    fun onClickSetPartOfEx() {
//        ExerciseSetBottomSheetFragment.getInstance()
//            .setOnClickOk {
//                viewModel.liveDatePartOfEx.postValue(it)
//            }.show(supportFragmentManager)
        ExPartSelectBottomSheetFragment.getInstance()
            .setOnClickOk {
            viewModel.liveDatePartOfEx.postValue(it)
            }.show(supportFragmentManager)
    }

    fun setTextStatus(position: Int){
        when (position) {
            1 -> setBtnView(everyDay)
            2 -> setBtnView(monday)
            3 -> setBtnView(tuesday)
            4 -> setBtnView(wendesday)
            5 -> setBtnView(thursday)
            6 -> setBtnView(friday)
            7 -> setBtnView(saturday)
            8 -> setBtnView(sunday)
        }
    }

    fun setBtnView(text: View) {

        val textList = arrayListOf<View>(everyDay, monday, tuesday, wendesday, thursday, friday, saturday, sunday)

        if (!text.isSelected) {
            textList.forEach { view ->
                if (view == text) text.isSelected = !text.isSelected
                else view.isSelected = false
            }
        }
    }

    fun replaceFrame(fragment: Fragment, container: Int) {
        val fmbt = supportFragmentManager.beginTransaction()

        fmbt.replace(container, fragment).commitAllowingStateLoss()
    }

}