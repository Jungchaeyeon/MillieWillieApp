package com.makeus.milliewillie.ui.plan

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.PlanVcBottomSheetBinding
import com.makeus.milliewillie.model.PlansRequest
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.numberpicker_bottom_sheet_holi.*
import kotlinx.android.synthetic.main.plan_vc_bottom_sheet.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


class PlanVacationBottomSheetFragment :
    BaseDataBindingBottomSheetFragment<PlanVcBottomSheetBinding>(R.layout.plan_vc_bottom_sheet) {


    val liveButton = MutableLiveData<String>()
    val viewModel: MakePlanViewModel by sharedViewModel()
    val repositoryCached by inject<RepositoryCached>()
    var hap = 0

    private var clickOk: ((String) -> Unit)? = null
    private lateinit var callback: OnBackPressedCallback

    companion object {
        fun getInstance() = PlanVacationBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyTransparentBottomSheetDialogTheme)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                ActivityNavigator.with(context).makeplan().start()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
    override fun PlanVcBottomSheetBinding.onBind() {
        vi = this@PlanVacationBottomSheetFragment
        vm = viewModel
    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): PlanVacationBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        viewModel.plansRequest.planVacation.add(PlansRequest.PlanVacation(1.toLong(),edit_regul.text.toString().toInt()))
        viewModel.plansRequest.planVacation.add(PlansRequest.PlanVacation(2.toLong(),edit_prize.text.toString().toInt()))
        viewModel.plansRequest.planVacation.add(PlansRequest.PlanVacation(3.toLong(),edit_other.text.toString().toInt()))

        Log.e(viewModel.plansRequest.planVacation[0].count.toString(),"frag planVac")
        Log.e(viewModel.plansRequest.planVacation[1].count.toString(),"frag planVac")
        Log.e(viewModel.plansRequest.planVacation[2].count.toString(),"frag planVac")
        Log.e(viewModel.plansRequest.planVacation[0].vacationId.toString(),"frag id")
        Log.e(viewModel.plansRequest.planVacation[1].vacationId.toString(),"frag id")
        Log.e(viewModel.plansRequest.planVacation[2].vacationId.toString(),"frag id")

        clickOk?.invoke("")
        dismiss()

    }



    fun onClickPlus(id: Int) {

        hap = edit_regul.text.toString().toInt() + edit_other.text.toString()
            .toInt() + edit_prize.text.toString().toInt()

        when (id) {
            0 -> edit_regul.text = (edit_regul.text.toString().toInt() + 1).toString()
            1 -> edit_prize.text = (edit_prize.text.toString().toInt() + 1).toString()
            2 -> edit_other.text = (edit_other.text.toString().toInt() + 1).toString()
            else -> ""
        }
        if (hap == repositoryCached.getAvailHoli() - 1) {
            btn_plus_other.isEnabled = false
            btn_plus_prize.isEnabled = false
            btn_plus_regul.isEnabled = false
        }


    }

    fun onClickMinus(id: Int) {
        Log.e("$hap", "hap")
        btn_plus_other.isEnabled = true
        btn_plus_prize.isEnabled = true
        btn_plus_regul.isEnabled = true

        if (edit_regul.text.toString().toInt() >= 0) {
            if (edit_prize.text.toString().toInt() >= 0) {
                if (edit_other.text.toString().toInt() >= 0) {
                    when (id) {
                        0 -> edit_regul.text = (edit_regul.text.toString().toInt() - 1).toString()
                        1 -> edit_prize.text = (edit_prize.text.toString().toInt() - 1).toString()
                        2 -> edit_other.text = (edit_other.text.toString().toInt() - 1).toString()
                        else -> ""
                    }
                }
            }
        }
        if (edit_regul.text.toString() == "-1") edit_regul.text = "0"
        if (edit_prize.text.toString() == "-1") edit_prize.text = "0"
        if (edit_other.text.toString() == "-1") edit_other.text = "0"

    }

    fun onClickCancel() {
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        txt_avail_num.text = repositoryCached.getAvailHoli().toString() + "일"
    }

}