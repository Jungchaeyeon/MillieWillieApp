package com.makeusteam.milliewillie.ui.intro

import android.os.Bundle
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityIntroServiceTypeDetailBinding
import com.makeusteam.milliewillie.databinding.ItemTypeDetailBinding
import com.makeusteam.milliewillie.model.ServiceDetailType
import com.makeusteam.milliewillie.model.UsersRequest
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_service_type_detail.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class IntroServiceTypeDetailActivity :
    BaseDataBindingActivity<ActivityIntroServiceTypeDetailBinding>(R.layout.activity_intro_service_type_detail) {
    val viewModel by viewModel<UserViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.usersRequest =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as UsersRequest
    }

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
                    })
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.requestDetailSoldier()
        }
//        if (repositoryCached.getType() == "????????????") {
//            viewModel.requestDetailSoldier()
//        } else if (repositoryCached.getType() == "?????????") {
//            viewModel.requestDetailSergeant()
//        } else viewModel.requestDetailCaptain()


    fun onClickItemD(detailType: String) {

        //?????? ?????? ?????? ?????? ??????
        viewModel.usersRequest.serveType = detailType
        Log.e(viewModel.usersRequest.stateIdx.toString(),"stateIdxDetail")
        repositoryCached.setValue(LocalKey.DETAILTYPE, detailType)
        ActivityNavigator.with(this).enlist1(viewModel.usersRequest).start()
//        if (repositoryCached.getType() == "????????????") {
//            ActivityNavigator.with(this).enlist1(viewModel.usersRequest).start()
//        } else {
//            ActivityNavigator.with(this).enlist2(viewModel.usersRequest).start()
//        }
    }

    fun onClickBack() {
        onBackPressed()
    }


}
