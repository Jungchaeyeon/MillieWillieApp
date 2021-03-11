package com.makeus.milliewillie.ui.bottom_navi_fragment

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutBinding
import com.makeus.milliewillie.ui.bottom_navi_fragment.HomeFragment


class WorkoutFragment :
    BaseDataBindingFragment<FragmentWorkoutBinding>(R.layout.fragment_workout) {

    companion object {
        fun getInstance() = WorkoutFragment()
    }

    override fun FragmentWorkoutBinding.onBind() {
        vi= this@WorkoutFragment
    }

    fun onClickItem() {
        val nextFrag = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, nextFrag, "findThisFragment")
            ?.addToBackStack(null)?.commit()
    }

}