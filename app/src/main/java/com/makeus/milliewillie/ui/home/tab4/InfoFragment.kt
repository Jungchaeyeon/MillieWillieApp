package com.makeus.milliewillie.ui.home.tab4

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.databinding.FragmentInfoBinding
import com.makeus.milliewillie.R
import com.makeus.milliewillie.ui.home.tab1.HomeFragment


class InfoFragment :

    BaseDataBindingFragment<FragmentInfoBinding>(R.layout.fragment_info) {

    companion object {
        fun getInstance() = InfoFragment()
    }

    override fun FragmentInfoBinding.onBind() {
        vi = this@InfoFragment
    }

    fun onClickItem() {
        val nextFrag = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, nextFrag, "findThisFragment")
            ?.addToBackStack(null)?.commit()
    }
}