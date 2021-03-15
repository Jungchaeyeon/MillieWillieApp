package com.makeus.milliewillie.ui

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityMainBinding
import com.makeus.milliewillie.ui.bottom_navi_fragment.EmotionFragment
import com.makeus.milliewillie.ui.bottom_navi_fragment.HomeFragment
import com.makeus.milliewillie.ui.bottom_navi_fragment.WorkoutFragment
import com.makeus.testmilliewillie.ui.bottom_navi_fragment.InfoFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {


    lateinit var fabOpen: Animation
    lateinit  var fabClose : Animation
    private var isFabOpen = false

    val viewModel by viewModel<MainViewModel>()
    companion object{
        fun getInstance() = MainActivity()
    }

    override fun ActivityMainBinding.onBind() {
        vi = this@MainActivity
        vm =viewModel

        fabOpen = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fab_close);

        changeFragment(HomeFragment.getInstance())

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_home -> {
                    fab.visibility = View.VISIBLE
                    changeFragment(HomeFragment.getInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.page_workout -> {
                    fab.visibility = View.GONE
                    changeFragment(WorkoutFragment.getInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.page_emotion -> {
                    fab.visibility = View.VISIBLE
                    changeFragment(EmotionFragment.getInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.page_info -> {
                    fab.visibility = View.VISIBLE
                    changeFragment(InfoFragment.getInstance())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }
    fun anim() {
        if (isFabOpen) {
            fab_dday.run {
                startAnimation(fabClose)
                isClickable =false
            }
            fab_plan.run{
                startAnimation(fabClose)
                isClickable =false
            }
            isFabOpen = false
        } else {
            fab_dday.run {
                startAnimation(fabOpen)
                isClickable =true
            }
            fab_plan.run{
                startAnimation(fabOpen)
                isClickable =true
            }
            isFabOpen = true
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
                ActivityNavigator.with(this).dDay().start()
            }
            R.id.fab_plan -> {
                anim()
                ActivityNavigator.with(this).makeplan().start()
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}