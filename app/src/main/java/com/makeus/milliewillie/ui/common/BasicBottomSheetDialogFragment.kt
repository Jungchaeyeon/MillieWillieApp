package com.makeus.milliewillie.ui.common

import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DialogBottomSheetBasicBinding


class BasicBottomSheetDialogFragment :
    BaseDataBindingBottomSheetFragment<DialogBottomSheetBasicBinding>(R.layout.dialog_bottom_sheet_basic) {

    val liveTitle = MutableLiveData<String>()
    val liveContent = MutableLiveData<String>()
    val liveButton = MutableLiveData<String>()
    private var clickOk: (() -> Unit)? = null
    
    companion object {
        fun getInstance() = BasicBottomSheetDialogFragment()
    }

    override fun DialogBottomSheetBasicBinding.onBind() {
        vi = this@BasicBottomSheetDialogFragment
        liveButton.postValue(context?.getString(R.string.ok))
    }

    fun setTitle(title: String): BasicBottomSheetDialogFragment {
        liveTitle.postValue(title)
        return this
    }

    fun setContent(content: String): BasicBottomSheetDialogFragment {
        liveContent.postValue(content)
        return this
    }

    fun setOnClickOk(clickOk: (() -> Unit)): BasicBottomSheetDialogFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke()
        dismiss()
    }
    
}