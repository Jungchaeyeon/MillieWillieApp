package com.makeus.milliewillie.ui.intro

import android.os.Bundle
import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroServiceTypeBinding
import com.makeus.milliewillie.model.UsersRequest
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
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