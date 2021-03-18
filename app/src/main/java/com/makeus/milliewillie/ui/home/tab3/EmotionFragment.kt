package com.makeus.milliewillie.ui.home.tab3

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentEmotionBinding
import com.makeus.milliewillie.ui.home.tab1.HomeFragment


class EmotionFragment :
    BaseDataBindingFragment<FragmentEmotionBinding>(R.layout.fragment_emotion) {

    companion object {
        fun getInstance() = EmotionFragment()
    }

    override fun FragmentEmotionBinding.onBind() {
        vi = this@EmotionFragment
    }

    fun onClickItem() {
        val nextFrag = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, nextFrag, "findThisFragment")
            ?.addToBackStack(null)?.commit()
    }
}