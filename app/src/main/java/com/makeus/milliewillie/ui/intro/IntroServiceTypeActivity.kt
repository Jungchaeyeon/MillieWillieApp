package com.makeus.milliewillie.ui.intro

import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroServiceTypeBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class IntroServiceTypeActivity :
    BaseDataBindingActivity<ActivityIntroServiceTypeBinding>(R.layout.activity_intro_service_type) {
    private val viewModel by viewModel<UserViewModel>()
    private val repositoryCached by inject<RepositoryCached>()
    override fun ActivityIntroServiceTypeBinding.onBind() {

        vi = this@IntroServiceTypeActivity
        vm = viewModel
        viewModel.bindLifecycle(this@IntroServiceTypeActivity)
    }

    fun onClickItem(view: View) {
        when (view.id) {
            R.id.type_soldier -> {
                repositoryCached.setValue(LocalKey.TYPE, "일반병사")
                viewModel.liveServiceId.value=1
            }
            R.id.type_sergeant ,R.id.type_captain,R.id.type_general-> {
                repositoryCached.setValue(LocalKey.TYPE, "부사관")
                viewModel.liveServiceId.value=2
            }
        }
        repositoryCached.getType().let { Log.d(it,"type") }
        ActivityNavigator.with(this).typedetail().start()
    }

    override fun onResume() {
        super.onResume()
    }
    fun onClickBack(){
      onBackPressed()
    }
}