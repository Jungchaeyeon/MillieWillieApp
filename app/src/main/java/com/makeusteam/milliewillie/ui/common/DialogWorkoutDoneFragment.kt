package com.makeusteam.milliewillie.ui.common

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.fragment.BaseDataBindingDialogFragment
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.DialogAddCancelBasicBinding
import com.makeusteam.milliewillie.util.Log

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
    private var callPlace: String = ""

    override fun DialogAddCancelBasicBinding.onBind() {
        vi = this@DialogWorkoutDoneFragment
        Log.e("callPlace = $callPlace")
        when (callPlace) {
            "start" -> liveButton.postValue(context?.getString(R.string.ok))
            "reports" -> liveButton.postValue(context?.getString(R.string.reports_delete))
        }
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

    fun setOnClickOk(clickOk: (() -> Unit), callPlace: String): DialogWorkoutDoneFragment {
        this.callPlace = callPlace
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