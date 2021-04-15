package com.makeus.milliewillie

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.makeus.milliewillie.model.PlansRequest
import com.makeus.milliewillie.model.UsersRequest
import com.makeus.milliewillie.ui.*
import com.makeus.milliewillie.ui.MainActivity
import com.makeus.milliewillie.ui.dDay.DdayActivity
import com.makeus.milliewillie.ui.holiday.HolidayActivity
import com.makeus.milliewillie.ui.holiday.HolidayEditActivity
import com.makeus.milliewillie.ui.info.AccountActivity
import com.makeus.milliewillie.ui.info.RuleWebViewActivity
import com.makeus.milliewillie.ui.intro.*
import com.makeus.milliewillie.ui.login.LoginActivity
import com.makeus.milliewillie.ui.map.MapActivity
import com.makeus.milliewillie.ui.mypage.InfoEnlistActivity
import com.makeus.milliewillie.ui.mypage.InfoMiliActivity
import com.makeus.milliewillie.ui.mypage.MyPageEditActivity
import com.makeus.milliewillie.ui.profile.PhotoSelectActivity
import com.makeus.milliewillie.ui.plan.*
import com.makeus.milliewillie.ui.profile.ProfileActivity
import com.makeus.milliewillie.ui.report.ReportActivity
import com.makeus.milliewillie.ui.routine.MakeRoutineActivity
import com.makeus.milliewillie.ui.todayWorkout.TodayWorkoutActivity
import com.makeus.milliewillie.ui.weightRecord.WeightRecordActivity
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartActivity
import java.util.*

/**
 * UI component 이외의 곳 (ViewModel, Service, 등) 에서 사용하지 않도록 주의할 것.
 * context 에 대한 reference 가 풀리지 않아 memory leak 이 발생할 수 있음.
 */
class ActivityNavigator private constructor(private val context: Context) {

    companion object {
        const val KEY_DATA = "data"

        @JvmStatic
        fun with(context: Context): ActivityNavigator = ActivityNavigator(context)

        @JvmStatic
        fun with(fragment: Fragment): ActivityNavigator =
            with(fragment.context ?: fragment.requireActivity())
    }

    val stack: ArrayList<Intent> = ArrayList()

    fun main(isClear: Boolean = true) = MyIntent(MainActivity::class.java, isClear)

    fun dDay() = MyIntent(DdayActivity::class.java)


    fun welcome(isClear: Boolean = true) = MyIntent(WelcomeActivity::class.java, isClear)
    fun login(isClear: Boolean = true) = MyIntent(LoginActivity::class.java, isClear)
    fun name() = MyIntent(IntroSettingNameActivity::class.java)
    fun type(usersRequest: UsersRequest) = MyIntent(IntroServiceTypeActivity::class.java).apply { putExtra(KEY_DATA,usersRequest) }
    fun typedetail(usersRequest: UsersRequest) = MyIntent(IntroServiceTypeDetailActivity::class.java).apply { putExtra(KEY_DATA,usersRequest) }
    fun enlist1(usersRequest: UsersRequest) = MyIntent(IntroEnlistDateSoldierActivity::class.java).apply { putExtra(KEY_DATA,usersRequest) }
    fun enlist2(usersRequest: UsersRequest) = MyIntent(IntroEnlistDateSergeantActivity::class.java).apply { putExtra(KEY_DATA,usersRequest) }
    fun map() = MyIntent(MapActivity::class.java)
    fun routine() = MyIntent(MakeRoutineActivity::class.java)
    fun makeplan() = MyIntent(MakePlanActivity::class.java)
    fun makeplanwith(isPatchPlan: String) = MyIntent(MakePlanActivity::class.java).apply { putExtra(KEY_DATA,isPatchPlan) }
    fun plancalendar(plansRequest: PlansRequest) = MyIntent(PlanCalendarActivity::class.java).apply { putExtra(KEY_DATA,plansRequest) }
    fun mypageedit() = MyIntent(MyPageEditActivity::class.java)
    fun maincalendar() = MyIntent(MainCalendarActivity::class.java)
    fun workoutStart() = MyIntent(WorkoutStartActivity::class.java)
    fun todayWorkout() = MyIntent(TodayWorkoutActivity::class.java)
    fun weightRecord() = MyIntent(WeightRecordActivity::class.java)
    fun holiday() = MyIntent(HolidayActivity::class.java)
    fun holiedit(liveHoliType: String) = MyIntent(HolidayEditActivity::class.java).apply { putExtra(KEY_DATA,liveHoliType) }
    fun infoenlist() = MyIntent(InfoEnlistActivity::class.java)
    fun infomili() = MyIntent(InfoMiliActivity::class.java)
    fun planoutput() = MyIntent(PlanOutputActivity::class.java)
    fun planvacation() = MyIntent(PlanVacationActivity::class.java)
    fun reports() = MyIntent(ReportActivity::class.java)
    fun profile() = MyIntent(ProfileActivity::class.java)
    fun photoSelect() = MyIntent(PhotoSelectActivity::class.java)
    fun account() = MyIntent(AccountActivity::class.java)
    fun rulesInAccount() = MyIntent(RuleWebViewActivity::class.java)
    fun planvacation(plansRequest: PlansRequest) = MyIntent(PlanVacationActivity::class.java).apply { putExtra(KEY_DATA,plansRequest) }
    fun plancalendaronlyone(plansRequest: PlansRequest) = MyIntent(PlanCalendarOnlyOneActivity::class.java).apply { putExtra(KEY_DATA,plansRequest) }
    fun goal(usersRequest: UsersRequest) = MyIntent(IntroGoalActivity::class.java).apply{
        putExtra(KEY_DATA, usersRequest)
    }

    inner class MyIntent : Intent {

        constructor(cls: Class<*>?, clear: Boolean = false) : super(context, cls) {
            addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT)
            addFlags(FLAG_ACTIVITY_SINGLE_TOP)

            if (clear) {
                addFlags(FLAG_ACTIVITY_NEW_TASK)
                addFlags(FLAG_ACTIVITY_CLEAR_TASK)
            }
        }

        constructor(action: String?, uri: Uri?) : super(action, uri)

        fun add(): ActivityNavigator {
            stack.add(this)
            return this@ActivityNavigator
        }


        fun start() {
            stack.add(this)
            context.startActivities(stack.toTypedArray())
        }

        fun startForResult(requestCode: Int) {
            if (context !is Activity) {
                throw IllegalArgumentException("startActivityForResult 는 context 가 activity 일 경우에만 사용할 수 있습니다.")
            }
            context.startActivityForResult(this, requestCode)
        }

    }
}