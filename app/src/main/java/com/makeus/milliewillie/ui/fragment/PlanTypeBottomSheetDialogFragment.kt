package com.makeus.milliewillie.ui.fragment

import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.PlanTypeBottomSheetBinding
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.MakePlanViewModel
import com.makeus.milliewillie.databinding.ItemPlanTypeBinding
import com.makeus.milliewillie.repository.local.LocalKey
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.item_plan_type.*
import kotlinx.android.synthetic.main.plan_type_bottom_sheet.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class PlanTypeBottomSheetDialogFragment :
    BaseDataBindingBottomSheetFragment<PlanTypeBottomSheetBinding>(R.layout.plan_type_bottom_sheet) {

    val viewModel by viewModel<MakePlanViewModel>()
    val liveButton = MutableLiveData<String>()
    private val repositoryCached by inject<RepositoryCached>()

    private var clickOk: (() -> Unit)? = null

    companion object {
        fun getInstance() = PlanTypeBottomSheetDialogFragment()
    }

    override fun PlanTypeBottomSheetBinding.onBind() {
        vi = this@PlanTypeBottomSheetDialogFragment
        vm = viewModel
        //vm.bindLifecycle(this@PlanTypeBottomSheetDialogFragment)
        liveButton.postValue(context?.getString(R.string.ok))

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        rv_plan_type.run {
            adapter = BaseDataBindingRecyclerViewAdapter<String>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<String, ItemPlanTypeBinding>(R.layout.item_plan_type) {
                        vi = this@PlanTypeBottomSheetDialogFragment
                        vm = viewModel
                        item = it
                    })
        }
    }

    fun setOnClickOk(clickOk: (() -> Unit)): PlanTypeBottomSheetDialogFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke()
        dismiss()
    }

    fun onClickItem(text: String) {
        //text.showShortToastSafe()
        repositoryCached.setValue(LocalKey.PLANTYPE, text)

    }


    override fun onResume() {
        super.onResume()
        viewModel.requestPlanTypeList()
    }

}