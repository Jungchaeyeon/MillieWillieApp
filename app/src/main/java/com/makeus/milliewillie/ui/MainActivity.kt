package com.makeus.milliewillie.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityMainBinding
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.home.tab1.HomeFragment
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment
import com.makeus.milliewillie.ui.home.tab3.EmotionFragment
import com.makeus.milliewillie.ui.home.tab4.InfoFragment
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel by viewModel<MainGetViewModel>()
    lateinit var fabOpen: Animation
    lateinit var fabClose: Animation
    lateinit var fabFastClose: Animation
    private var isFabOpen = false
    val repositoryCached by inject<RepositoryCached>()
    companion object {
        fun getInstance() = MainActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ExerciseId 앱 실행시 싱글톤으로 1회 실행
        viewModel.apiRepository.getExerciseId()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("getExerciseId 호출 성공")
                    // 받아온 exId Local 저장
                    repositoryCached.setValue(LocalKey.EXERCISEID, it.result)
                    Log.e("getExerciseId = ${repositoryCached.getExerciseId()}")
                } else {
                    Log.e("getExerciseId 호출 실패")
                    Log.e(it.message)
                }
            }.disposeOnDestroy(this@MainActivity)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun ActivityMainBinding.onBind() {
        vi = this@MainActivity



        fabOpen = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fab_close);
        fabFastClose = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fab_close_fast);

        initNavigation()
        changeFragment(HomeFragment.getInstance())
        navigationView.setOnNavigationItemSelectedListener { item ->
            fabAction()
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
                    fab.visibility = View.GONE
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

    private fun initNavigation() {
        //bottom navi 기본 tint 설정 막음
        binding.navigationView.itemIconTintList = null

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
                SampleToast.createToast(this, getString(R.string.toast_update_later))?.show()
//                anim()
//                ActivityNavigator.with(this).dDay().start()
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

    fun changeFragment(fragment: Fragment) {
        alpha90.visibility = View.GONE
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
//            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        SampleToast.createToast(this,"한번 더 클릭하면 앱이 종료됩니다.")?.show()
    }

}