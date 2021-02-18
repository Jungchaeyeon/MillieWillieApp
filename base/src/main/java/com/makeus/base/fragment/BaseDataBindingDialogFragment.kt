package com.makeus.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager

abstract class BaseDataBindingDialogFragment<B : ViewDataBinding>(private val layoutId: Int) :
    BaseDialogFragment(layoutId) {

    protected lateinit var binding: B

    abstract fun B.onBind()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun setupView(view: View) {
        binding.lifecycleOwner = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onBind()
    }

    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, this::class.java.name)
    }
}