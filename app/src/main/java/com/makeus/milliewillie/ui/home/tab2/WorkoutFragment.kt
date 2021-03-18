package com.makeus.milliewillie.ui.home.tab2

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutBinding
import com.makeus.milliewillie.ui.fragment.DatePickerBirthBottomSheetDialogFragment
import com.makeus.milliewillie.ui.home.tab1.HomeFragment
import com.makeus.milliewillie.util.Log


class WorkoutFragment :
    BaseDataBindingFragment<FragmentWorkoutBinding>(R.layout.fragment_workout) {

    companion object {
        fun getInstance() = WorkoutFragment()
    }

    override fun FragmentWorkoutBinding.onBind() {
        vi= this@WorkoutFragment

        WeightRecordBottomSheetFragment.getInstance()
            .setOnClickOk {goal, current ->

            }.show(fragmentManager!!)

    }

    fun onClickItem() {
        val nextFrag = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, nextFrag, "findThisFragment")
            ?.addToBackStack(null)?.commit()
    }

    fun onClick() {
        ActivityNavigator.with(context!!).routine().start()
    }

}