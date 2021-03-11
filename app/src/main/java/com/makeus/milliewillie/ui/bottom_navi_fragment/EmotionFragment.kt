package com.makeus.milliewillie.ui.bottom_navi_fragment

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentEmotionBinding


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