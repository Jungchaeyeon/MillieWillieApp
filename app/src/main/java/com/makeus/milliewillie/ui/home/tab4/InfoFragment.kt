package com.makeus.milliewillie.ui.home.tab4

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.databinding.FragmentInfoBinding
import com.makeus.milliewillie.R
import com.makeus.milliewillie.ui.home.tab1.HomeFragment
import org.koin.android.viewmodel.ext.android.viewModel


class InfoFragment : BaseDataBindingFragment<FragmentInfoBinding>(R.layout.fragment_info) {

    private val viewModel by viewModel<InfoViewModel>()

    companion object {
        fun getInstance() = InfoFragment()
    }

    override fun FragmentInfoBinding.onBind() {
        vi = this@InfoFragment
        vm = viewModel

        getLoginType()
    }

    fun getLoginType() {
//        when (loginType) {
//            MyApplication.LOGINTYPE.KAKAO -> {
//                binding.infoTextLoginType.text = getString(R.string.kakao_login)
//            }
//            MyApplication.LOGINTYPE.GOOGLE -> {
//                binding.infoTextLoginType.text = getString(R.string.google_login)
//            }
//        }
    }

    fun onClickItem() {
        val nextFrag = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, nextFrag, "findThisFragment")
            ?.addToBackStack(null)?.commit()
    }
}