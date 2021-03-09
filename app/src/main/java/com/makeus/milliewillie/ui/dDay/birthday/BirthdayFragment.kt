package com.makeus.milliewillie.ui.dDay.birthday

import android.util.Log
import android.widget.Toast
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.MyApplication.Companion.isFocused
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayAnniversaryBinding
import com.makeus.milliewillie.databinding.FragmentDDayBirthdayBinding
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel

class BirthdayFragment: BaseDataBindingFragment<FragmentDDayBirthdayBinding>(R.layout.fragment_d_day_birthday) {

    private val viewModel by viewModel<BirthdayViewModel>()

    override fun FragmentDDayBirthdayBinding.onBind() {
        vi = this@BirthdayFragment
        vm = viewModel
        viewModel.bindLifecycle(this@BirthdayFragment)

        binding.dDayCertiEditTodo.setOnFocusChangeListener { v, hasFocus ->
            isFocused = hasFocus
            com.makeus.milliewillie.util.Log.e(isFocused.toString())
            if (!hasFocus) {
                Toast.makeText(context, "Focus off", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(context, "Focus on", Toast.LENGTH_SHORT).show()
            }

        }

    }
}