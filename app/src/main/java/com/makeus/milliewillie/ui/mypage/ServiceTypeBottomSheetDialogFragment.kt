package com.makeus.milliewillie.ui.mypage

import android.os.Bundle
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ItemServiceTypeBinding
import com.makeus.milliewillie.databinding.ServiceTypeBottomSheetBinding
import com.makeus.milliewillie.model.ServiceDetailType
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.intro.UserViewModel
import com.makeus.milliewillie.ui.plan.PlanTypeBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.item_plan_type.*
import kotlinx.android.synthetic.main.plan_type_bottom_sheet.*
import kotlinx.android.synthetic.main.service_type_bottom_sheet.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class ServiceTypeBottomSheetDialogFragment :
    BaseDataBindingBottomSheetFragment<ServiceTypeBottomSheetBinding>(R.layout.service_type_bottom_sheet) {

    val viewModel by viewModel<UserViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    private var clickType : ((String) -> Unit)? = null

    companion object {
        fun getInstance() = ServiceTypeBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun ServiceTypeBottomSheetBinding.onBind(){

        vi = this@ServiceTypeBottomSheetDialogFragment
        vm = viewModel

        rv_service_type.run {
            adapter = BaseDataBindingRecyclerViewAdapter<ServiceDetailType>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<ServiceDetailType, ItemServiceTypeBinding>(R.layout.item_service_type) {
                        vi = this@ServiceTypeBottomSheetDialogFragment
                        item = it.detailType
                    })
            }
    }

    fun setOnClickType(clickType: ((String) -> Unit)): ServiceTypeBottomSheetDialogFragment{
        this.clickType = clickType
        return this }

    fun onClickType(text: String) {
        clickType?.invoke(text)
        dismiss()
    }

    override fun onResume(){
        super.onResume()
        if(repositoryCached.getType()=="일반병사") viewModel.requestDetailSoldier()
        else if(repositoryCached.getType()=="부사관") viewModel.requestDetailSergeant()
        else viewModel.requestDetailCaptain()
    }

}