package com.makeusteam.milliewillie.ui.common

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.fragment.BaseDataBindingDialogFragment
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.DialogBasicBinding

class BasicDialogFragment :
        BaseDataBindingDialogFragment<DialogBasicBinding>(R.layout.dialog_basic) {

    companion object {
        fun getInstance() = BasicDialogFragment()
    }

    val liveTitle = MutableLiveData<String>()
    val liveSubTitle = MutableLiveData<String>().apply { value = "" }
    val liveContent = MutableLiveData<String>()
    val liveButton = MutableLiveData<String>()
    private var clickOk: (() -> Unit)? = null

    override fun DialogBasicBinding.onBind() {
        vi = this@BasicDialogFragment
        liveButton.postValue(context?.getString(R.string.ok))
    }

    fun setTitle(title: String): BasicDialogFragment {
        liveTitle.postValue(title)
        return this
    }

    fun setSubTitle(subTitle: String): BasicDialogFragment {
        liveSubTitle.postValue(subTitle)
        return this
    }

    fun setContent(content: String): BasicDialogFragment {
        liveContent.postValue(content)
        return this
    }

    fun setOnClickOk(clickOk: (() -> Unit)): BasicDialogFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke()
        dismiss()
    }
}