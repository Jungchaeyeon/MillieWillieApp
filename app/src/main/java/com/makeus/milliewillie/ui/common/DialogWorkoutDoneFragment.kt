package com.makeus.milliewillie.ui.common

import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingDialogFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DialogAddCancelBasicBinding

class DialogWorkoutDoneFragment :
        BaseDataBindingDialogFragment<DialogAddCancelBasicBinding>(R.layout.dialog_add_cancel_basic) {

    companion object {
        fun getInstance() = DialogWorkoutDoneFragment()
    }

    val liveTitle = MutableLiveData<String>()
    val liveSubTitle = MutableLiveData<String>().apply { value = "" }
    val liveContent = MutableLiveData<String>()
    val liveButton = MutableLiveData<String>()
    val liveCancelButton = MutableLiveData<String>()
    private var clickOk: (() -> Unit)? = null

    override fun DialogAddCancelBasicBinding.onBind() {
        vi = this@DialogWorkoutDoneFragment
        liveButton.postValue(context?.getString(R.string.ok))
        liveCancelButton.postValue(context?.getString(R.string.cancel))
    }

    fun setTitle(title: String): DialogWorkoutDoneFragment {
        liveTitle.postValue(title)
        return this
    }

    fun setSubTitle(subTitle: String): DialogWorkoutDoneFragment {
        liveSubTitle.postValue(subTitle)
        return this
    }

    fun setContent(content: String): DialogWorkoutDoneFragment {
        liveContent.postValue(content)
        return this
    }

    fun setOnClickOk(clickOk: (() -> Unit)): DialogWorkoutDoneFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke()
        dismiss()
    }

    fun onClickCancel() {
        dismiss()
    }
}