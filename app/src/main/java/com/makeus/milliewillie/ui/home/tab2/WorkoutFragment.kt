package com.makeus.milliewillie.ui.home.tab2

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutBinding
import com.makeus.milliewillie.ui.home.tab1.HomeFragment


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