package com.makeusteam.milliewillie.ui.mypage

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.BirthModifyFragmentBinding
import com.makeusteam.milliewillie.ui.intro.UserViewModel
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


class MyPageBirthFragment :
    BaseDataBindingBottomSheetFragment<BirthModifyFragmentBinding>(R.layout.birth_modify_fragment) {

    val viewModel :UserViewModel by sharedViewModel()
    val liveButton = MutableLiveData<String>()

    private var date : String = ""

    private var clickOk: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = MyPageBirthFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun BirthModifyFragmentBinding.onBind() {
        vi = this@MyPageBirthFragment
        vm=viewModel
        liveButton.postValue(context?.getString(R.string.ok))

        val today = Calendar.getInstance()
        dpSpinner.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            date = "$year.${month + 1}.$day"
            viewModel.liveEditData.postValue(date)
        }


    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): MyPageBirthFragment {
        this.clickOk = clickOk
        return this
    }


    fun onClickOk() {
        view
        clickOk?.invoke(date)
        dismiss()
    }
    fun onClickCancel(){
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        viewModel.liveUserBirth.apply { value="생년월일을 입력해주세요" }
    }

}