package com.makeus.milliewillie.ui.routine

import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.model.PostRoutineRequest
import com.makeus.milliewillie.model.RoutineSelectedRecyclerItem
import com.makeus.milliewillie.model.RoutineWorkoutListItem
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.EXERCISE_ID
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.absItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.armItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.backItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.chestItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.legItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.shoulderItemListKey
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class MakeRoutineActivity: BaseDataBindingActivity<ActivityMakeRoutineBinding>(R.layout.activity_make_routine) {

    private val viewModel by viewModel<MakeRoutineViewModel>()

    lateinit var everyDay: View
    lateinit var monday: View
    lateinit var tuesday: View
    lateinit var wendesday: View
    lateinit var thursday: View
    lateinit var friday: View
    lateinit var saturday: View
    lateinit var sunday: View

    private var exerciseName = ""
    private var position by Delegates.notNull<Int>()

    private var detailType = ArrayList<Int>()
    private var detailTypeContext = ArrayList<String>()
    private var detailSetEqual = ArrayList<Boolean>()
    private var detailSet = ArrayList<Int>()

    override fun ActivityMakeRoutineBinding.onBind() {
        vi = this@MakeRoutineActivity
        vm = viewModel
        viewModel.bindLifecycle(this@MakeRoutineActivity)

        everyDay = binding.routineTextEveryday
        monday = binding.routineTextMonday
        tuesday = binding.routineTextTuesday
        wendesday = binding.routineTextWednesday
        thursday = binding.routineTextThursday
        friday = binding.routineTextFriday
        saturday = binding.routineTextSaturday
        sunday = binding.routineTextSunday

        everyDay.isSelected = true

        routineSelectedRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<RoutineSelectedRecyclerItem>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<RoutineSelectedRecyclerItem, RoutineWorkoutSelectedRecyclerItemBinding>(R.layout.routine_workout_selected_recycler_item) {
                        vi = this@MakeRoutineActivity
                        item = it
                    }
                )
        }
        routineWorkoutList.run {
            // 아이템 클릭 리스너
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    val position = child?.let { rv.getChildAdapterPosition(it) }
                    if (e.action == MotionEvent.ACTION_MOVE) return false
                    else if (e.action == MotionEvent.ACTION_UP) {
                        Log.e("$position")
                        if (position != null) {
                            this@MakeRoutineActivity.position = position
                            return false
                        }
                        return true
                    }
                    return false
                }
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })

            adapter = BaseDataBindingRecyclerViewAdapter<RoutineWorkoutListItem>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<RoutineWorkoutListItem, RoutineWorkoutListItemBinding>(R.layout.routine_workout_list_item) {
                        vi = this@MakeRoutineActivity
                        item = it
                    }
                )
        }

    }

    fun onClickExItem() {
        when (viewModel.exerciseKind) {
            "전체" -> exerciseName = viewModel.totalItemList[position]
            "하체" -> exerciseName = viewModel.legItemList[position]
            "가슴" -> exerciseName = viewModel.chestItemList[position]
            "등" -> exerciseName = viewModel.backItemList[position]
            "어깨" -> exerciseName = viewModel.shoulderItemList[position]
            "팔" -> exerciseName = viewModel.armItemList[position]
            "복근" -> exerciseName = viewModel.absItemList[position]
        }

        ExerciseSetBottomSheetFragment.getInstance()
            .setOnClickOk(exerciseName) { name, list, detailTypeList, detailTypeContextList, detailSetEqualList, detailSetList ->
                var textOption = ""
                for (i in 0 until list.size) {
                    if (i == list.size-1) {
                        textOption += list[i]
                    } else {
                        textOption += "${list[i]} · "
                    }
                }
                detailType = detailTypeList
                detailTypeContext = detailTypeContextList
                detailSetEqual = detailSetEqualList
                detailSet = detailSetList
                Log.e("detailSet = $detailSet")
                viewModel.addSelectedItem(RoutineSelectedRecyclerItem(routineName = name, routineSetOptions = textOption))
                Log.e("${RoutineSelectedRecyclerItem(routineName = name, routineSetOptions = textOption)}")
            }.show(supportFragmentManager)

    }


    fun onClickSetPartOfEx() {
        ExPartSelectBottomSheetFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDatePartOfEx.postValue(it)
                val textSet = String.format(getString(R.string.part_of_ex_title, it))
                viewModel.liveDataPartOfExTitle.postValue(textSet)
                viewModel.createExItem(it)
            }.show(supportFragmentManager)

    }

    fun setTextStatus(position: Int){
        when (position) {
            1 -> setBtnView(everyDay)
            2 -> setBtnView(monday)
            3 -> setBtnView(tuesday)
            4 -> setBtnView(wendesday)
            5 -> setBtnView(thursday)
            6 -> setBtnView(friday)
            7 -> setBtnView(saturday)
            8 -> setBtnView(sunday)
        }
    }

    fun setBtnView(text: View) {
        val textList = arrayListOf<View>(everyDay, monday, tuesday, wendesday, thursday, friday, saturday, sunday)

        if (text == everyDay) {
            text.isSelected = true
            textList.forEach {
                if (it != everyDay) it.isSelected = false
            }
        } else {
            everyDay.isSelected = false
            text.isSelected = !text.isSelected
        }

    }

    fun onClickAddExKind() {
        AddExerciseBottomSheetFragment.getInstance()
            .setOnClickOk {
                viewModel.totalItemList.add(it)
                when (viewModel.exerciseKind) {
                    "하체" -> {
                        viewModel.legItemList.add(it)
                        SharedPreference.putArrayStringItem(legItemListKey, viewModel.legItemList)
                    }
                    "가슴" -> {
                        viewModel.chestItemList.add(it)
                        SharedPreference.putArrayStringItem(chestItemListKey, viewModel.chestItemList)
                    }
                    "등" -> {
                        viewModel.backItemList.add(it)
                        SharedPreference.putArrayStringItem(backItemListKey, viewModel.backItemList)
                    }
                    "어깨" -> {
                        viewModel.shoulderItemList.add(it)
                        SharedPreference.putArrayStringItem(shoulderItemListKey, viewModel.shoulderItemList)
                    }
                    "팔" -> {
                        viewModel.armItemList.add(it)
                        SharedPreference.putArrayStringItem(armItemListKey, viewModel.armItemList)
                    }
                    "복근" -> {
                        viewModel.absItemList.add(it)
                        SharedPreference.putArrayStringItem(absItemListKey, viewModel.absItemList)
                    }
                }
            }.show(supportFragmentManager)
    }


    var repeatDays = ""

    fun getRepeatDays() {
        val textList = arrayListOf<View>(everyDay, monday, tuesday, wendesday, thursday, friday, saturday, sunday)

        for (i in 0 until textList.size) {
            if (textList[i].isSelected) {
                when (textList[i]) {
                    everyDay -> {
                        repeatDays += "8"
                        break
                    }
                    monday -> {
                        repeatDays += "1"
                        if (i != 0 && i != textList.size-1) repeatDays += "#"
                    }
                    tuesday -> {
                        repeatDays += "2"
                        if (i != 0 && i != textList.size-1) repeatDays += "#"
                    }
                    wendesday -> {
                        repeatDays += "3"
                        if (i != 0 && i != textList.size-1) repeatDays += "#"
                    }
                    thursday -> {
                        repeatDays += "4"
                        if (i != 0 && i != textList.size-1) repeatDays += "#"
                    }
                    friday -> {
                        repeatDays += "5"
                        if (i != 0 && i != textList.size-1) repeatDays += "#"
                    }
                    saturday -> {
                        repeatDays += "6"
                        if (i != 0 && i != textList.size-1) repeatDays += "#"
                    }
                    sunday -> {
                        repeatDays += "7"
                        if (i != 0 && i != textList.size-1) repeatDays += "#"
                    }
                }
            }

        }
    }

    fun onClickOk() {
        getRepeatDays()
        val detailNameList = ArrayList<String>()

        viewModel.liveDataSelectedItemList.value!!.forEach {
            detailNameList.add(it.routineName)
        }

        Log.e(binding.routineEditRoutineName.text.toString())
        Log.e(viewModel.liveDatePartOfEx.value.toString())
        Log.e(repeatDays)
        Log.e(detailNameList.toString())
        Log.e(detailType.toString())
        Log.e(detailTypeContext.toString())
        Log.e(detailSetEqual.toString())
        Log.e(detailSet.toString())

        viewModel.apiRepository.postRoutine(body = PostRoutineRequest(
            routineName = binding.routineEditRoutineName.text.toString(),
            bodyPart = viewModel.liveDatePartOfEx.value.toString(),
            repeatDay = repeatDays,
            detailName = detailNameList,
            detailType = detailType,
            detailTypeContext = detailTypeContext,
            detailSetEqual = detailSetEqual,
            detailSet = detailSet)
            , path = SharedPreference.getSettingItem(EXERCISE_ID)!!.toLong())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("postRoutine 호출 성공")
                } else {
                    Log.e("postRoutine 호출 실패")
                    Log.e(it.message)
                }
            }.disposeOnDestroy(this)

        onBackPressed()
    }

    fun onClickCancel() {
        onBackPressed()
    }

}