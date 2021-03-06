package com.makeusteam.milliewillie.ui.intro

import android.view.View
import android.view.animation.TranslateAnimation
import androidx.viewpager2.widget.ViewPager2
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityWelcomeBinding
import com.makeusteam.milliewillie.databinding.ItemWelcomeBinding
import com.makeusteam.milliewillie.model.Intro
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.item_welcome.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class WelcomeActivity : BaseDataBindingActivity<ActivityWelcomeBinding>(R.layout.activity_welcome) {
    private val viewModel by viewModel<WelcomeViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    override fun ActivityWelcomeBinding.onBind() {
        vi = this@WelcomeActivity
        vm = viewModel
        viewModel.bindLifecycle(this@WelcomeActivity)
        vIndicator.createDotPanel(2, R.drawable.indicator_dot_off, R.drawable.indicator_dot_on, 0)



        //viewpager
        vp_intro.run {
            adapter = BaseDataBindingRecyclerViewAdapter<Intro>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Intro, ItemWelcomeBinding>(R.layout.item_welcome) {
                        item = it
                    })

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    vIndicator.selectDot(position)

                    if(position==0){
                        btn_startMW.visibility = View.INVISIBLE
                    }
                     if (position == 1) {
                        btn_startMW.visibility = View.VISIBLE
                        val animate = TranslateAnimation(
                            0F,  // fromXDelta
                            0F,  // toXDelta
                            btn_startMW.height.toFloat(),  // fromYDelta
                            0F
                        ) // toYDelta
                        animate.duration = 500
                        btn_startMW.startAnimation(animate)
                    }
                }
            })
        }
    }

    fun onClickNext() {
        val layouts = 2
        val current: Int = getItem(1)
        if (current < layouts) {
            // ?????? ????????????
            vp_intro.currentItem = current
        } else {
            ActivityNavigator.with(this).name().start()
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

