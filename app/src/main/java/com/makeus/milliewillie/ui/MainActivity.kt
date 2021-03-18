package com.makeus.milliewillie.ui

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityMainBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.home.tab1.HomeFragment
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment
import com.makeus.milliewillie.ui.home.tab3.EmotionFragment
import com.makeus.milliewillie.ui.home.tab4.InfoFragment
import com.makeus.milliewillie.ui.intro.UserViewModel
import com.makeus.milliewillie.ui.plan.MakePlanViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {


    lateinit var fabOpen: Animation
    lateinit var fabClose: Animation
    lateinit var fabFastClose: Animation
    private var isFabOpen = false
    val viewModel by viewModel<UserViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    companion object {
        fun getInstance() = MainActivity()
    }

    override fun ActivityMainBinding.onBind() {
        vi = this@MainActivity
        viewModel.bindLifecycle(this@MainActivity)

        fabOpen = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fab_close);
        fabFastClose = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fab_close_fast);

        initNavigation()
        changeFragment(HomeFragment.getInstance())
        navigationView.setOnNavigationItemSelectedListener { item ->
            fabAction()
            when (item.itemId) {
                R.id.page_home -> {

                    changeFragment(HomeFragment.getInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.page_workout -> {

                    changeFragment(WorkoutFragment.getInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.page_emotion -> {

                    changeFragment(EmotionFragment.getInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.page_info -> {

                    changeFragment(InfoFragment.getInstance())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }

    private fun initNavigation() {
        //bottom navi 기본 tint 설정 막음
        navigation_view.itemIconTintList = null

    }

    fun anim() {
        if (isFabOpen) {
            fab_dday.run {
                startAnimation(fabClose)
                isClickable = false
            }
            fab_plan.run {
                startAnimation(fabClose)
                isClickable = false
            }
            isFabOpen = false
            alpha90.visibility = View.GONE
        } else {
            fab_dday.run {
                startAnimation(fabOpen)
                isClickable = true
            }
            fab_plan.run {
                startAnimation(fabOpen)
                isClickable = true
            }
            isFabOpen = true
            alpha90.visibility = View.VISIBLE
        }
    }

    fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.fab -> {
                anim()
            }
            R.id.fab_dday -> {
                anim()
            }
            R.id.fab_plan -> {
                fab_dday.run {
                    startAnimation(fabFastClose)
                    isClickable = false
                }
                fab_plan.run {
                    startAnimation(fabFastClose)
                    isClickable = false
                }
                ActivityNavigator.with(this).makeplan().start()
                isFabOpen = false
                alpha90.visibility = View.GONE

            }
        }
    }

    fun fabAction() {
        if (isFabOpen) {
            fab_dday.startAnimation(fabClose)
            fab_plan.startAnimation(fabClose)
            isFabOpen = false
        }
        alpha90.visibility = View.GONE
    }

    private fun changeFragment(fragment: Fragment) {
        alpha90.visibility = View.GONE
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
    }
}