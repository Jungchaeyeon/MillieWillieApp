package com.makeus.milliewillie.ui.mypage

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityMyPageBinding

class MyPageActivity : BaseDataBindingActivity<ActivityMyPageBinding>(R.layout.activity_my_page){

    override fun ActivityMyPageBinding.onBind() {
        vi=this@MyPageActivity
    }
    fun onClickEdit(){
        ActivityNavigator.with(this).mypageedit().start()
    }
}