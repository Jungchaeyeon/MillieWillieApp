package com.makeusteam.milliewillie.ui.intro

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityIntroGoalBinding
import com.makeusteam.milliewillie.model.UsersRequest
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.SampleToast
import com.makeusteam.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_goal.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class IntroGoalActivity :
    BaseDataBindingActivity<ActivityIntroGoalBinding>(R.layout.activity_intro_goal) {

    val viewModel by viewModel<UserViewModel>()
    val context = this
    val repositoryCached by inject<RepositoryCached>()
    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.usersRequest =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as UsersRequest
    }
    override fun ActivityIntroGoalBinding.onBind() {
        vi = this@IntroGoalActivity
//        Log.e(viewModel.usersRequest.startDate.toString(),"startDate")
//        Log.e(viewModel.usersRequest.endDate.toString(),"endDate")
//        Log.e(viewModel.usersRequest.proDate.toString(),"proDate")
//        Log.e(viewModel.usersRequest.strCorporal.toString(),"strCorporal")
//        Log.e(viewModel.usersRequest.strPrivate.toString(),"strPrivate")
//        Log.e(viewModel.usersRequest.strSergeant.toString(),"strSergeant")
    }

    fun onClickBack() {
        onBackPressed()
    }

    fun onClickDone() {
//        repositoryCached.setValue(LocalKey.TOKEN,"")
//        repositoryCached.setValue(LocalKey.ISMEMBER, false)
//        repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, false)
//        repositoryCached.setValue(LocalKey.ISINPUTGOAL, false)
//        repositoryCached.setValue(LocalKey.EXERCISEID, (-1))
//        repositoryCached.setValue(LocalKey.POSTYEAR, 10000)
//        repositoryCached.setValue(LocalKey.POSTMONTH, 100)
//        repositoryCached.setValue(LocalKey.POSTDAY, 100)

        if (edt_goal.text.isNotEmpty()) {
            viewModel.usersRequest.goal=edt_goal.text.toString()
            requestUser()
        } else {
            Snackbar.make(this.linearLayout,"????????? ????????? ?????????.",Snackbar.LENGTH_SHORT).show()
        }
    }
    fun requestUser() {
        viewModel.requestUser().subscribe {
            if (it.isSuccess) {
                repositoryCached.setValue(LocalKey.ISINPUTGOAL, false)
                repositoryCached.setValue(LocalKey.TOKEN, it.result.jwt)
                repositoryCached.setValue(LocalKey.ISMEMBER, true)
                repositoryCached.setValue(LocalKey.POSTYEAR, 10000)
                repositoryCached.setValue(LocalKey.POSTMONTH, 100)
                repositoryCached.setValue(LocalKey.POSTDAY, 100)


                Log.e(repositoryCached.getIsMember().toString(),"ISMEMBER")
                Log.e(repositoryCached.getToken(), "??????users")

                SampleToast.createToast(this,"???????????? ????????? ??????????????? :)")?.show()
                if(repositoryCached.getIsMember()==true) {ActivityNavigator.with(this).main().start()}
                else{
                    ActivityNavigator.with(this).login().start()
                }
//                Log.e( it.result.jwt, "?????? Test")
//                Log.e( it.result.userId.toString(),"id ???")
            } else {
                repositoryCached.setValue(LocalKey.TOKEN,"")
                ActivityNavigator.with(this).name().start()
                SampleToast.createToast(this,"???????????? ????????? ??????????????????")?.show()
            }
        }.disposeOnDestroy(this)
    }





}