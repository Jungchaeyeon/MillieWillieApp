package com.makeus.milliewillie.ui.intro

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroServiceTypeDetailBinding
import com.makeus.milliewillie.databinding.ItemTypeDetailBinding
import com.makeus.milliewillie.model.ServiceDetailType
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_intro_service_type_detail.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class IntroServiceTypeDetailActivity :
    BaseDataBindingActivity<ActivityIntroServiceTypeDetailBinding>(R.layout.activity_intro_service_type_detail) {
    val viewModel by viewModel<UserViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    override fun ActivityIntroServiceTypeDetailBinding.onBind() {
        vi = this@IntroServiceTypeDetailActivity
        vm = viewModel
        viewModel.bindLifecycle(this@IntroServiceTypeDetailActivity)

        rv_detail_item.run {
            adapter = BaseDataBindingRecyclerViewAdapter<ServiceDetailType>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<ServiceDetailType, ItemTypeDetailBinding>(
                        R.layout.item_type_detail
                    ) {
                        vi = this@IntroServiceTypeDetailActivity
                        item = it
                        repositoryCached.setValue(LocalKey.DETAILTYPE, it.detailType.toString())
                    })
        }

    }

    override fun onResume() {
        super.onResume()
//        Log.d("typetest1",repositoryCached.getType())
//        Log.d("typetest2",repositoryCached.getType().compareTo("일반병사").toString())

        if (repositoryCached.getType() == "일반병사") {
            viewModel.requestDetailSoldier()
        } else if (repositoryCached.getType() == "부사관") {
            viewModel.requestDetailSergeant()
        } else viewModel.requestDetailCaptain()
    }

    fun onClickItemD(detailType: String) {

        //복무 세부 타입 캐시 저장
        repositoryCached.setValue(LocalKey.DETAILTYPE, detailType)
        viewModel.liveServiceype.postValue(detailType)
        if (repositoryCached.getType() == "일반병사") {
            ActivityNavigator.with(this).enlist1().start()
        } else {
            ActivityNavigator.with(this).enlist2().start()
        }
    }

    fun onClickBack() {
        onBackPressed()
    }
}
