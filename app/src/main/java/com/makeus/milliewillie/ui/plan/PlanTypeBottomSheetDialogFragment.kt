package com.makeus.milliewillie.ui.plan

import android.os.Bundle
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ItemPlanTypeBinding
import com.makeus.milliewillie.databinding.PlanTypeBottomSheetBinding
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.item_plan_type.*
import kotlinx.android.synthetic.main.plan_type_bottom_sheet.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class PlanTypeBottomSheetDialogFragment :
    BaseDataBindingBottomSheetFragment<PlanTypeBottomSheetBinding>(R.layout.plan_type_bottom_sheet) {

    val viewModel by viewModel<MakePlanViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    private var clickDate: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = PlanTypeBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun PlanTypeBottomSheetBinding.onBind() {
        vi = this@PlanTypeBottomSheetDialogFragment
        vm = viewModel

        rv_plan_type.run {
            adapter = BaseDataBindingRecyclerViewAdapter<String>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<String, ItemPlanTypeBinding>(R.layout.item_plan_type) {
                        vi = this@PlanTypeBottomSheetDialogFragment
                        item = it
                    })
        }
    }

    fun setOnClickDate(clickDate: ((String) -> Unit)): PlanTypeBottomSheetDialogFragment {
        this.clickDate = clickDate
        return this
    }

    fun onClickDate(text: String) {
        viewModel.livePlanType.postValue(text)
        repositoryCached.setValue(LocalKey.PLANTYPE, text)
        clickDate?.invoke(text)
        dismiss()
    }


    override fun onResume() {
        super.onResume()
        viewModel.requestPlanTypeList()
    }

}