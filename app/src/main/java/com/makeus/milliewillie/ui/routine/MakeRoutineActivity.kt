package com.makeus.milliewillie.ui.routine

import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.isModifiedRoutine
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.absItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.armItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.backItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.chestItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.legItemListKey
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel.Companion.shoulderItemListKey
import com.makeus.milliewillie.ui.todayWorkout.TodayWorkoutFeedFragment.Companion.ROUTINE_ID_KEY
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class MakeRoutineActivity: BaseDataBindingActivity<ActivityMakeRoutineBinding>(R.layout.activity_make_routine) {

    private val viewModel by viewModel<MakeRoutineViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    lateinit var everyDay: View
    lateinit var monday: View
    lateinit var tuesday: View
    lateinit var wendesday: View
    lateinit var thursday: View
    lateinit var friday: View
    lateinit var saturday: View
    lateinit var sunday: View

    private var routineId by Delegates.notNull<Long>()
    private var exerciseName = ""
    private var position by Delegates.notNull<Int>()
    private var selectedListPosition by Delegates.notNull<Int>()

    private var detailType = ArrayList<Int>()
    private var detailTypeContext = ArrayList<String>()
    private var detailTypePatchContext = ArrayList<String>()
    private var detailSetEqual = ArrayList<Boolean>()
    private var detailSet = ArrayList<Int>()

    private var isPartOfEx: Boolean = false

    var repeatDays = ""

    override fun onResume() {
        super.onResume()
        if (intent.hasExtra(ROUTINE_ID_KEY)) {
            routineId = intent.getLongExtra(ROUTINE_ID_KEY, 0)
        }

        if (isModifiedRoutine) {
            Log.e("isModifiedRoutine = $isModifiedRoutine")
            executeGetDetailsExercises()
        }

    }

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
                            this@MakeRoutineActivity.selectedListPosition = position
                            return false
                        }
                        return true
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
            adapter = BaseDataBindingRecyclerViewAdapter<RoutineSelectedRecyclerItem>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<RoutineSelectedRecyclerItem, RoutineWorkoutSelectedRecyclerItemBinding>(
                        R.layout.routine_workout_selected_recycler_item
                    ) {
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
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<RoutineWorkoutListItem, RoutineWorkoutListItemBinding>(
                        R.layout.routine_workout_list_item
                    ) {
                        vi = this@MakeRoutineActivity
                        item = it
                    }
                )
        }

    }

    private fun executePostRoutine() {
        val detailNameList = ArrayList<String>()

        viewModel.liveDataSelectedItemList.value!!.forEach {
            detailNameList.add(it.routineName)
        }

        viewModel.apiRepository.postRoutine(
            body = PostRoutineRequest(
                routineName = binding.routineEditRoutineName.text.toString(),
                bodyPart = viewModel.liveDatePartOfEx.value.toString(),
                repeatDay = repeatDays,
                detailName = detailNameList,
                detailType = detailType,
                detailTypeContext = detailTypeContext,
                detailSetEqual = detailSetEqual,
                detailSet = detailSet
            ), path = repositoryCached.getExerciseId()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("postRoutine 호출 성공")
                } else {
                    Log.e("postRoutine 호출 실패")
                    Log.e(it.message)
                }
                onBackPressed()
            }.disposeOnDestroy(this)
    }

    private fun executePatchRoutine() {
        val detailNameList = ArrayList<String>()

        viewModel.liveDataSelectedItemList.value!!.forEach {
            detailNameList.add(it.routineName)
        }
        viewModel.apiRepository.patchRoutine(
            exerciseId = repositoryCached.getExerciseId(),
            routineId = routineId,
            body = PostRoutineRequest(
                routineName = binding.routineEditRoutineName.text.toString(),
                bodyPart = viewModel.liveDatePartOfEx.value.toString(),
                repeatDay = repeatDays,
                detailName = detailNameList,
                detailType = detailType,
                detailTypeContext = detailTypePatchContext,
                detailSetEqual = detailSetEqual,
                detailSet = detailSet
            ))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log
                if (it.isSuccess) {
                    Log.e("patchRoutine 호출 성공")

                } else {
                    Log.e("patchRoutine 호출 실패")
                    Log.e(it.message)
                }
                isModifiedRoutine = false
                onBackPressed()
            }.disposeOnDestroy(this)
    }


    lateinit var routineName: String
    lateinit var bodyPart: String
    var repeatDay = ArrayList<Int>()
    var detailResList = ArrayList<ExerciseDetailRes>()

    var resExerciseName = ""
    val exerciseNameList = ArrayList<String>()
    var resExerciseType = 0
    val exerciseTypeList = ArrayList<Int>()
    var resSetCount = 0
    var resIsSetSame = false

    private fun executeGetDetailsExercises() {
        viewModel.apiRepository.getDetailsExercises(
            exerciseId = repositoryCached.getExerciseId(),
            routineId = routineId
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("executeGetDetailsExercises 호출 성공")
                    var index = 0

                    routineName = it.result.get("routineName").asString
                    bodyPart = it.result.get("bodyPart").asString
                    isPartOfEx = true

                    val repeatList = it.result.get("repeatDay").asJsonArray
                    for (i in 0 until repeatList.size()) repeatDay.add(repeatList[i].asInt)

                    val resDetailResList = it.result.get("detailResList").asJsonArray

                    resDetailResList.forEach { objects ->
                        val item = objects.asJsonObject

                        val setDetailList = ArrayList<ExerciseDetailSetRes>()

                        resExerciseName = item.get("exerciseName").asString
                        resExerciseType = item.get("exerciseType").asInt
                        resSetCount = item.get("setCount").asInt
                        resIsSetSame = item.get("isSetSame").asBoolean

                        exerciseTypeList.add(resExerciseType)
                        exerciseNameList.add(resExerciseName)
                        item.get("setDetailList").asJsonArray.forEach{ setDetailsObject ->
                            val setDetailsItem = setDetailsObject.asJsonObject

                            val setStr = setDetailsItem.get("setStr").asString
                            val weight = setDetailsItem.get("weight").asDouble
                            val count = setDetailsItem.get("count").asInt
                            val time = setDetailsItem.get("time").asInt

                            val listItem = ExerciseDetailSetRes(setStr = setStr, weight = weight, count = count, time = time)

                            setDetailList.add(listItem)
                        }
                        val listItem2 = ExerciseDetailRes(
                            exerciseName = resExerciseName,
                            exerciseType = resExerciseType,
                            setCount = resSetCount,
                            isSetSame = resIsSetSame,
                            setDetailList = setDetailList
                        )
                        detailResList.add(listItem2)

                        detailSetEqual.add(resIsSetSame)
                        detailType.add(resExerciseType)
                        initResult(index)
                        index++
                    }

                    modifiedViewDataBinding()
                } else {
                    Log.e("executeGetDetailsExercises 호출 실패")
                    Log.e(it.message)
                }
            }.disposeOnDestroy(this)

    }

    private fun initResult(idx: Int) {
        when (detailType[idx]) {
            1 -> {
                detailSet.add(detailResList[idx].setCount)
                when (detailSetEqual[idx]) {
                    true -> {
                        detailResList[idx].setDetailList.forEach { inner ->
                                if (detailResList[idx].exerciseType == 1) {
                                    detailTypePatchContext.add("${inner.weight}#${inner.count}")
                                    detailTypeContext.add("${inner.weight}#${inner.count}")
                                }
                        }
                        Log.e(detailTypeContext.toString())
                    }
                    false -> {
                        var index = 0
                        var optionsText = ""
                        detailResList[idx].setDetailList.forEach { inner ->
                                if (detailResList[idx].exerciseType == 1) {
                                    optionsText += if (index == 0) "${inner.weight}#${inner.count}"
                                    else "/${inner.weight}#${inner.count}"
                                    index++
                                }
                        }
                        detailTypePatchContext.add(optionsText)
                        detailTypeContext.add(optionsText)
                        Log.e(detailTypeContext.toString())
                    }
                }
            }
            2 -> {
                detailSet.add(detailResList[idx].setCount)
                when (detailSetEqual[idx]) {
                    true -> {
                        detailResList[idx].setDetailList.forEach { inner ->
                            if (detailResList[idx].exerciseType == 2) {
                                detailTypePatchContext.add("${inner.count}")
                                detailTypeContext.add("${inner.count}")
                            }
                        }
                        Log.e(detailTypeContext.toString())
                    }
                    false -> {
                        var index = 0
                        var optionsText = ""
                        detailResList[idx].setDetailList.forEach { inner ->
                            if (detailResList[idx].exerciseType == 2) {
                                optionsText += if (index == 0) "${inner.count}"
                                else "/${inner.count}"
                                index++
                            }
                        }
                        detailTypePatchContext.add(optionsText)
                        detailTypeContext.add(optionsText)
                        Log.e(detailTypeContext.toString())
                    }
                }
            }
            3 -> {
                detailSet.add(detailResList[idx].setCount)
                Log.e("detailSetEqual[idx] = ${detailSetEqual[idx]}")
                when (detailSetEqual[idx]) {
                    true -> {
                        detailResList[idx].setDetailList.forEach { inner ->
                            if (detailResList[idx].exerciseType == 3) {
                                val timeText = decodeTime(inner.time)
                                detailTypeContext.add(timeText)
                                detailTypePatchContext.add((inner.time).toString())
                                Log.e("time = ${inner.time}")
                            }
                        }
                        Log.e("detailTypePatchContext = $detailTypePatchContext")
                        Log.e(detailTypeContext.toString())
                    }
                    false -> {
                        var index = 0
                        var optionsText = ""
                        var timeSum = ""
                        detailResList[idx].setDetailList.forEach { inner ->
                            Log.e("$index time = ${inner.time}")
                            if (detailResList[idx].exerciseType == 3) {
                                val timeText = decodeTime(inner.time)
                                if (index == 0) {
                                    optionsText += timeText
                                    timeSum += "${inner.time}"
                                } else {
                                    optionsText += "/$timeText"
                                    timeSum += "/${inner.time}"
                                }
                                index++
                            }

                        }
                        detailTypePatchContext.add(timeSum)
                        Log.e("optionsText = $optionsText")
                        Log.e("detailTypePatchContext = $detailTypePatchContext")
                        detailTypeContext.add(optionsText)
                        Log.e(detailTypeContext.toString())
                    }

                }
            }
        }
    }


    fun modifiedViewDataBinding() {
        everyDay.isSelected = false
        binding.routineEditRoutineName.setText(routineName)
        viewModel.liveDatePartOfEx.postValue(bodyPart)

        // repeat
        repeatDay.forEach {
            when (it) {
                1 -> monday.isSelected = true
                2 -> tuesday.isSelected = true
                3 -> wendesday.isSelected = true
                4 -> thursday.isSelected = true
                5 -> friday.isSelected = true
                6 -> saturday.isSelected = true
                7 -> sunday.isSelected = true
                8 -> everyDay.isSelected = true
            }
        }

        var typeIndex = 0
        detailResList.forEach {
            var routineSetOption = ""
            var idx = 0
            it.setDetailList.forEach { inner ->
                when (exerciseTypeList[typeIndex]) {
                    1 -> {
                        if (idx == 0) routineSetOption += "${inner.setStr}, ${inner.weight}kg, ${inner.count}개"
                        else routineSetOption += " · ${inner.setStr}, ${inner.weight}kg, ${inner.count}개"
                    }
                    2 -> {
                        if (idx == 0) routineSetOption += "${inner.setStr}, ${inner.count}개"
                        else routineSetOption += " · ${inner.setStr}, ${inner.count}개"
                    }
                    3 -> {
                        val timeText = decodeTime(inner.time)
                        if (idx == 0) routineSetOption += "${inner.setStr}, $timeText"
                        else routineSetOption += " · ${inner.setStr},$timeText"
                    }
                }
                idx ++
            }

            val selectedItem = RoutineSelectedRecyclerItem(routineName = exerciseNameList[typeIndex], routineSetOptions = routineSetOption)
            viewModel.addSelectedItem(selectedItem)
            typeIndex++
        }

        val textSet = String.format(getString(R.string.part_of_ex_title, bodyPart))
        viewModel.liveDataPartOfExTitle.postValue(textSet)
        viewModel.createExItem(bodyPart)

    }

    private var m = 0
    private var h = 0
    private var s = 0
    private fun decodeTime(sec: Int): String {
        var timeText = ""

        m = sec / 60
        h = m / 60
        s = 0
        s = sec % 60
        m %= 60

        if (h != 0) timeText += "${h}시간 "
        if (m != 0) timeText += "${m}분 "
        if (s != 0) timeText += "${s}초 "

        return timeText
    }

    private fun incodeTime(detailTypeContext: ArrayList<String>): Int {
        var timeSum = 0
        for (i in 0 until detailTypeContext.size) {
            if (detailTypeContext[i] == "시") {
                timeSum += (detailTypeContext[i-1] + detailTypeContext[i-2]).toInt() * 3600
            } else if (detailTypeContext[i] == "분") {
                timeSum += (detailTypeContext[i-1] + detailTypeContext[i-2]).toInt() * 60
            } else if (detailTypeContext[i] == "초") {
                timeSum += (detailTypeContext[i-1] + detailTypeContext[i-2]).toInt()
            }
        }
        Log.e("timeSum = $timeSum")
        return timeSum
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
                for (i in 0 until detailTypeList.size) {
                    detailType.add(detailTypeList[i])
                    detailTypePatchContext.add(detailTypeContextList[i])
                    detailTypeContext.add(detailTypeContextList[i])
                    detailSetEqual.add(detailSetEqualList[i])
                    detailSet.add(detailSetList[i])
                }
                viewModel.addSelectedItem(RoutineSelectedRecyclerItem(routineName = name, routineSetOptions = textOption))

            }.show(supportFragmentManager)

    }


    fun onClickSetPartOfEx() {
        if (!isPartOfEx) {
            ExPartSelectBottomSheetFragment.getInstance()
                .setOnClickOk {
                    viewModel.liveDatePartOfEx.postValue(it)
                    val textSet = String.format(getString(R.string.part_of_ex_title, it))
                    viewModel.liveDataPartOfExTitle.postValue(textSet)
                    viewModel.createExItem(it)
                    isPartOfEx = true
                }.show(supportFragmentManager)
        }


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
        val textList = arrayListOf<View>(
            everyDay,
            monday,
            tuesday,
            wendesday,
            thursday,
            friday,
            saturday,
            sunday
        )

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
                        SharedPreference.putArrayStringItem(
                            chestItemListKey,
                            viewModel.chestItemList
                        )
                    }
                    "등" -> {
                        viewModel.backItemList.add(it)
                        SharedPreference.putArrayStringItem(backItemListKey, viewModel.backItemList)
                    }
                    "어깨" -> {
                        viewModel.shoulderItemList.add(it)
                        SharedPreference.putArrayStringItem(
                            shoulderItemListKey,
                            viewModel.shoulderItemList
                        )
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



    fun getRepeatDays() {
        repeatDays = ""
        val textList = arrayListOf<View>(everyDay, monday, tuesday, wendesday, thursday, friday, saturday, sunday)
        var idx = 0
        for (i in 0 until textList.size) {
            if (textList[i].isSelected) {
                when (textList[i]) {
                    everyDay -> {
                        repeatDays = "8"
                        break
                    }
                    monday -> {
                        if (idx != 0 && idx != textList.size - 1) repeatDays += "#1"
                        else repeatDays += "1"
                        idx++
                    }
                    tuesday -> {
                        if (idx != 0 && idx != textList.size - 1) repeatDays += "#2"
                        else repeatDays += "2"
                        idx++
                    }
                    wendesday -> {
                        if (idx != 0 && idx != textList.size - 1) repeatDays += "#3"
                        else repeatDays += "3"
                        idx++
                    }
                    thursday -> {
                        if (idx != 0 && idx != textList.size - 1) repeatDays += "#4"
                        else repeatDays += "4"
                        idx++
                    }
                    friday -> {
                        if (idx != 0 && idx != textList.size - 1) repeatDays += "#5"
                        else repeatDays += "5"
                        idx++
                    }
                    saturday -> {
                        if (idx != 0 && idx != textList.size - 1) repeatDays += "#6"
                        else repeatDays += "6"
                        idx++
                    }
                    sunday -> {
                        if (idx != 0 && idx != textList.size - 1) repeatDays += "#7"
                        else repeatDays += "7"
                        idx++
                    }
                }
            }
            Log.e("repeatDays = $repeatDay")
        }
    }

    fun onClickRemoveItem() {
        detailSet.removeAt(selectedListPosition)
        detailSetEqual.removeAt(selectedListPosition)
        detailType.removeAt(selectedListPosition)
        detailTypeContext.removeAt(selectedListPosition)
        detailTypePatchContext.removeAt(selectedListPosition)

        viewModel.removeSelectedItem(selectedListPosition)
    }

    fun onClickOk() {
        getRepeatDays()
        when (isModifiedRoutine) {
            true -> {
                val isDoNotExecute = setDoNotExecute()
                if (!isDoNotExecute) executePatchRoutine()
                else SampleToast.createToast(this, getString(R.string.toast_input_routine_data))?.show()
            }
            false -> {
                val isDoNotExecute = setDoNotExecute()
                if (!isDoNotExecute) executePostRoutine()
                else SampleToast.createToast(this, getString(R.string.toast_input_routine_data))?.show()
            }
        }

    }

    private fun setDoNotExecute(): Boolean {
        var setIsDoNotExecute = false
        for (i in 0 until detailType.size) {
            when (detailType[i]) {
                1 -> {
                    if (detailTypeContext[i] == "#") {
                        setIsDoNotExecute = true
                        break
                    }
                }
                2 -> {
                    if (detailTypeContext[i] == "") {
                        setIsDoNotExecute = true
                        break
                    }
                }
                3 -> {
                    if (detailTypeContext[i] == "0") {
                        setIsDoNotExecute = true
                        break
                    }
                }
            }
        }
        return setIsDoNotExecute
    }

    fun onClickCancel() {
        onBackPressed()
    }

}
