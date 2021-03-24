package com.makeus.milliewillie.ui.plan

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DatepickerBottomSheetBasicBinding
import com.makeus.milliewillie.databinding.NumberpickerBottomSheetHoliBinding
import com.makeus.milliewillie.databinding.PlanVcBottomSheetBinding
import com.makeus.milliewillie.di.repositoryModule
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.intro.UserViewModel
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.numberpicker_bottom_sheet_holi.*
import kotlinx.android.synthetic.main.plan_vc_bottom_sheet.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.compat.SharedViewModelCompat
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class PlanVacationBottomSheetFragment :
    BaseDataBindingBottomSheetFragment<PlanVcBottomSheetBinding>(R.layout.plan_vc_bottom_sheet) {


    val liveButton = MutableLiveData<String>()
    val viewModel : MakePlanViewModel by sharedViewModel()
    val repositoryCached by inject<RepositoryCached>()

    private var clickOk: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = PlanVacationBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun PlanVcBottomSheetBinding.onBind() {
         vi=this@PlanVacationBottomSheetFragment
    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): PlanVacationBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke("")
        dismiss()
    }
    fun onClickPlus(id : Int){

        when(id){
            0-> edt_regul.text = (edt_regul.text.toString().toInt()+1).toString()
            1-> edt_prize.text = (edt_prize.text.toString().toInt()+1).toString()
            2-> edt_other.text = (edt_other.text.toString().toInt()+1).toString()
            else -> ""
        }
        val hap =edt_regul.text.toString().toInt()+ edt_other.text.toString().toInt()+ edt_prize.text.toString().toInt()
        Log.e(repositoryCached.getAvailHoli().toString(),"AvailHoli repo")
        if(hap >=repositoryCached.getAvailHoli())
        {
            btn_plus_other.isEnabled=false
            btn_plus_prize.isEnabled=false
            btn_plus_regul.isEnabled=false
        }
    }
    fun onClickMinus(num: Int){

        when(id){
            0-> edt_regul.text = (edt_regul.text.toString().toInt()-1).toString()
            1-> edt_prize.text = (edt_prize.text.toString().toInt()-1).toString()
            2-> edt_other.text = (edt_other.text.toString().toInt()-1).toString()
            else -> ""
        }
        if(edt_regul.text =="0") edt_regul.isEnabled=false
        else if(edt_prize.text =="0") edt_prize.isEnabled=false
        else if(edt_other.text =="0") edt_other.isEnabled=false
        else ""
    }

    fun onClickCancel() {
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        txt_avail_num.text=repositoryCached.getAvailHoli().toString()+"일"
    }

}