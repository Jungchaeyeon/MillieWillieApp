package com.makeus.milliewillie.ui

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityWelcomeBinding
import com.makeus.milliewillie.databinding.ItemWelcomeBinding
import com.makeus.milliewillie.model.Intro
import kotlinx.android.synthetic.main.activity_welcome.*
import org.koin.android.viewmodel.ext.android.viewModel

class WelcomeActivity : BaseDataBindingActivity<ActivityWelcomeBinding>(R.layout.activity_welcome) {
    private val viewModel by viewModel<WelcomeViewModel>()

    override fun ActivityWelcomeBinding.onBind() {
        vi = this@WelcomeActivity
        vm = viewModel
        viewModel.bindLifecycle(this@WelcomeActivity)


        //viewpager
        vp_intro.run {
            adapter = BaseDataBindingRecyclerViewAdapter<Intro>()
                .setItemViewType { item, position, isLast ->
                    if (position == 0) 0 else 0
                }
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Intro, ItemWelcomeBinding>(R.layout.item_welcome) {
                        item = it
                    })
        }
    }

    fun onClickNext() {
        val layouts = 2
        val current: Int = getItem(1)
        //current.toString().showShortToastSafe()
        if (current < layouts) {
            // 다음 화면이동
            vp_intro.currentItem = current
        } else {
            ActivityNavigator.with(this).login().start()
        }
    }
    private fun getItem(i: Int): Int {
        return vp_intro.currentItem + i
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestIntroItemList()
    }
}
