package com.makeus.milliewillie.ui.mypage

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentMypageEditBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.intro.UserViewModel
import kotlinx.android.synthetic.main.activity_my_page_edit.*
import kotlinx.android.synthetic.main.fragment_mypage_edit.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel


class MyPageEditFragment :
    BaseDataBindingFragment<FragmentMypageEditBinding>(R.layout.fragment_mypage_edit) {

    val viewModel: UserViewModel by sharedViewModel()
    val repositoryCached by inject<RepositoryCached>()

    companion object {
        fun getInstance() = MyPageEditFragment()
    }


    override fun FragmentMypageEditBinding.onBind() {
        vi = this@MyPageEditFragment
        vm = viewModel
        val value = viewModel.liveModifyTitle.value.toString()

        var maxLenth = 10
        edit_txt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when (value) {
                    "이름" -> maxLenth = 10
                    "목표" -> maxLenth = 20
                }
                (edit_txt.length() <= maxLenth).also {
                    btn_ok.isEnabled = it
                    btn_ok.setTextColor(Color.parseColor("#b2babf"))
                }
                if(edit_txt.length() > maxLenth) "$maxLenth 자 내로 설정해주세요.".showShortToastSafe()


            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mypage_modify_container.setOnTouchListener { p0, p1 -> true }

    }

    fun onClickOk() {

        when (viewModel.liveModifyTitle.value.toString()) {
            "이름" -> {
                viewModel.liveUserName.postValue(viewModel.liveEditData.value.toString())
                // viewModel.liveUserName.value.toString().showShortToastSafe()
            }
            "목표" -> {
                viewModel.liveUserGoal.postValue(viewModel.liveEditData.value.toString())
                repositoryCached.setValue(LocalKey.GOAL,viewModel.liveEditData.value.toString())
            }
        }

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.detach(this)
            ?.commit()
    }

    fun onClickCancel() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.detach(this)
            ?.commit()
    }


}
