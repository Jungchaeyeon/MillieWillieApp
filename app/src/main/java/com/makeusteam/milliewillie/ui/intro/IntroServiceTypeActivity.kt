package com.makeusteam.milliewillie.ui.intro

import android.os.Bundle
import android.view.View
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityIntroServiceTypeBinding
import com.makeusteam.milliewillie.model.UsersRequest
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class IntroServiceTypeActivity :
    BaseDataBindingActivity<ActivityIntroServiceTypeBinding>(R.layout.activity_intro_service_type) {

    val viewModel by viewModel<UserViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.usersRequest =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as UsersRequest
    }

    override fun ActivityIntroServiceTypeBinding.onBind() {

        vi = this@IntroServiceTypeActivity
        vm = viewModel
        viewModel.bindLifecycle(this@IntroServiceTypeActivity)

    }

    fun onClickItem(view: View) {

        when (view.id) {
            R.id.type_soldier -> {
                repositoryCached.setValue(LocalKey.TYPE, "일반병사")

                viewModel.usersRequest.stateIdx =1
            }
            R.id.type_sergeant -> {
                repositoryCached.setValue(LocalKey.TYPE, "부사관")

                viewModel.usersRequest.stateIdx =2
            }
            R.id.type_captain,R.id.type_general ->{
                repositoryCached.setValue(LocalKey.TYPE, "장교")

                viewModel.usersRequest.stateIdx =3
            }
        }
        ActivityNavigator.with(this).typedetail(viewModel.usersRequest).start()

    }

    fun onClickBack(){
      onBackPressed()
    }


}