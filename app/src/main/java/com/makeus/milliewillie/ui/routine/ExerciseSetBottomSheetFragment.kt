package com.makeus.milliewillie.ui.routine

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.RoutineExBottomSheetBinding
import com.makeus.milliewillie.databinding.RoutineExCountRecyclerItemBinding
import com.makeus.milliewillie.databinding.RoutineExTimeRecyclerItemBinding
import com.makeus.milliewillie.databinding.RoutineExWncRecyclerItemBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.WorkoutCountSet
import com.makeus.milliewillie.model.WorkoutTimeSet
import com.makeus.milliewillie.model.WorkoutWncSet
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.routine_ex_bottom_sheet.*
import kotlinx.android.synthetic.main.routine_ex_wnc_recycler_item.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class ExerciseSetBottomSheetFragment:
    BaseDataBindingBottomSheetFragment<RoutineExBottomSheetBinding>(R.layout.routine_ex_bottom_sheet) {

    enum class SetOptions() {
        WNC,
        COUNT,
        TIME
    }

    companion object {
        fun getInstance() = ExerciseSetBottomSheetFragment()
    }

    val viewModel by viewModel<ExerciseSetViewModel>()
    private var exerciseName = ""
    private var setOptionKind = SetOptions.WNC
    private var position by Delegates.notNull<Int>()

    lateinit var btnWnc: View
    lateinit var btnCount: View
    lateinit var btnTime: View

    var resultArrayList = ArrayList<String>()

    private val detailType = ArrayList<Int>()
    private val detailTypeContext = ArrayList<String>()
    private val detailSetEqual = ArrayList<Boolean>()
    private val detailSet = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments.let {
//            exerciseName = it?.get(EXERCISE_NAME).toString()
//        }
        viewModel.liveDataExerciseName.postValue(exerciseName)
        position = 0
    }

    private var clickOk: ((String, ArrayList<String>, ArrayList<Int>,ArrayList<String>, ArrayList<Boolean>, ArrayList<Int>) -> Unit)? = null

    var temp =""

    override fun RoutineExBottomSheetBinding.onBind() {
        vi = this@ExerciseSetBottomSheetFragment
        vm = viewModel
        viewModel.bindLifecycle(this@ExerciseSetBottomSheetFragment)

        btnWnc = binding.rebsBtnWnc
        btnCount = binding.rebsBtnCount
        btnTime = binding.rebsBtnTime

        setBtnStatus(1)
        binding.rebsSwitch.isChecked = true

        // 리사이클러뷰 바인딩 적용 3종
        rebsWncRecyclerSet.run {
            getPositionInRecycler(this)
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutWncSet>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutWncSet, RoutineExWncRecyclerItemBinding>(R.layout.routine_ex_wnc_recycler_item) {
                        vi = this@ExerciseSetBottomSheetFragment
                        item = it
                        var weightValue = ""
                        var countValue = ""
                        this.rebsWncRecyclerEditWeight.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                weightValue = s.toString()
                            }
                            override fun afterTextChanged(s: Editable?) {
//                                if (s.isNullOrBlank()) {
//                                    "값을 입력해야합니다.".showShortToastSafe()
//                                } else {
//                                    if (s.isNullOrBlank()) weightValue = "0"
//                                    Log.e(weightValue)
//                                    viewModel.addPositionItem(SetOptions.WNC, position, weightValue, 1)
//                                }
                            }
                        })
                        this.rebsWncRecyclerEditCount.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                countValue = s.toString()
                            }
                            override fun afterTextChanged(s: Editable?) {
//                                if (s.isNullOrBlank()) {
//                                    "값을 입력해야합니다.".showShortToastSafe()
//                                } else {
                                    if (s.isNullOrBlank()) countValue = "0"
                                    Log.e(countValue)
                                    viewModel.addPositionItem(SetOptions.WNC, position, countValue, 2)
//                                }
                            }
                        })
                    }
                )
        }
        rebsCountRecyclerSet.run {
            getPositionInRecycler(this)
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutCountSet>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutCountSet, RoutineExCountRecyclerItemBinding>(R.layout.routine_ex_count_recycler_item) {
                        vi = this@ExerciseSetBottomSheetFragment
                        item = it

                        var countValue = ""
                        this.rebsCountRecyclerEditCount.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                countValue = s.toString()
                            }
                            override fun afterTextChanged(s: Editable?) {
//                                if (s.isNullOrBlank()) {
//                                    "값을 입력해야합니다.".showShortToastSafe()
//                                } else {
                                    if (s.isNullOrBlank()) countValue = "0"
                                    Log.e(countValue)
                                    viewModel.addPositionItem(SetOptions.COUNT, position, countValue, 1)
//                                }
                            }
                        })

                    }
                )
        }
        rebsTimeRecyclerSet.run {
            getPositionInRecycler(this)
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutTimeSet>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutTimeSet, RoutineExTimeRecyclerItemBinding>(R.layout.routine_ex_time_recycler_item) {
                        vi = this@ExerciseSetBottomSheetFragment
                        item = it

                        var hourValue = ""
                        var minValue = ""
                        var secValue = ""
                        this.rebsTimeRecyclerEditHour.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                hourValue = s.toString()
                            }
                            override fun afterTextChanged(s: Editable?) {
//                                if (s.isNullOrBlank() && binding.rebsSwitch.isChecked) {
//                                    "값을 입력해야합니다.".showShortToastSafe()
//                                } else {
                                    if (s.isNullOrBlank()) hourValue = "0"
                                    Log.e(hourValue)
                                    viewModel.addPositionItem(SetOptions.TIME, position, hourValue, 1)
//                                }
                            }
                        })
                        this.rebsTimeRecyclerEditMin.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                minValue = s.toString()
                            }
                            override fun afterTextChanged(s: Editable?) {
//                                if (s.isNullOrBlank() && binding.rebsSwitch.isChecked) {
//                                    "값을 입력해야합니다.".showShortToastSafe()
//                                } else {
                                    if (s.isNullOrBlank()) minValue = "0"
                                    Log.e(minValue)
                                    viewModel.addPositionItem(SetOptions.TIME, position, minValue, 2)
//                                }
                            }
                        })
                        this.rebsTimeRecyclerEditSec.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                secValue = s.toString()
                            }
                            override fun afterTextChanged(s: Editable?) {
//                                if (s.isNullOrBlank() && binding.rebsSwitch.isChecked) {
//                                    "값을 입력해야합니다.".showShortToastSafe()
//                                } else {
                                    if (s.isNullOrBlank()) secValue = "0"
                                    Log.e(secValue)
                                    viewModel.addPositionItem(SetOptions.TIME, position, secValue, 3)
//                                }
                            }
                        })

                    }
                )
        }

    }

    fun getPositionInRecycler(rv: RecyclerView) {
        // get position 터치 리스너
        rv.run {
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    val position = child?.let { rv.getChildAdapterPosition(it) }
                    if (e.action == MotionEvent.ACTION_MOVE) return false
                    else if (e.action == MotionEvent.ACTION_UP) {
                        if (position != null) {
                            this@ExerciseSetBottomSheetFragment.position = position
                            return false
                        }
                        return true
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }

    fun setBtnStatus(position: Int){
        when (position) {
            1 -> {
                setBtnView(btnWnc)
                replaceViewFrame(SetOptions.WNC)
            }
            2 -> {
                setBtnView(btnCount)
                replaceViewFrame(SetOptions.COUNT)
            }
            3 -> {
                setBtnView(btnTime)
                replaceViewFrame(SetOptions.TIME)
            }
        }
    }

    fun setBtnView(btn: View) {
        val btnList = arrayListOf<View>(btnWnc, btnCount, btnTime)

        if (!btn.isSelected) {
            btnList.forEach { view ->
                if (view == btn) btn.isSelected = !btn.isSelected
                else view.isSelected = false
            }
        }
    }

    fun replaceViewFrame(setOptions: SetOptions) {
        this.setOptionKind = setOptions
        when (setOptions) {
            SetOptions.WNC -> {
                binding.rebsWncLayout.visibility = View.VISIBLE
                binding.rebsCountLayout.visibility = View.GONE
                binding.rebsTimeLayout.visibility = View.GONE
            }
            SetOptions.COUNT -> {
                binding.rebsWncLayout.visibility = View.GONE
                binding.rebsCountLayout.visibility = View.VISIBLE
                binding.rebsTimeLayout.visibility = View.GONE
            }
            SetOptions.TIME -> {
                binding.rebsWncLayout.visibility = View.GONE
                binding.rebsCountLayout.visibility = View.GONE
                binding.rebsTimeLayout.visibility = View.VISIBLE
            }

        }

    }

    fun onClickAddItem(view: View) {
        when (view.id) {
            R.id.rebs_btn_plus -> {
                viewModel.increaseSetCount("${viewModel.liveDataSetCount.value!!.toInt() + 1}")
            }
            R.id.rebs_btn_minus -> {
                viewModel.decreaseSetCount("${viewModel.liveDataSetCount.value!!.toInt() - 1}")
            }
        }

    }

    fun onClickSwitch() {
        when (binding.rebsSwitch.isChecked) {
            true -> {
                viewModel.resetItems()

                binding.rebsWncLayoutInput.visibility = View.VISIBLE
                binding.rebsCountLayoutInput.visibility = View.VISIBLE
                binding.rebsTimeLayoutInput.visibility = View.VISIBLE

                binding.rebsWncRecyclerSet.visibility = View.GONE
                binding.rebsCountRecyclerSet.visibility = View.GONE
                binding.rebsTimeRecyclerSet.visibility = View.GONE
            }
            false -> {
                binding.rebsWncEditWeight.text = null
                binding.rebsWncEditCount.text = null
                binding.rebsCountEditCount.text = null
                binding.rebsTimeEditHour.text = null
                binding.rebsTimeEditMin.text = null
                binding.rebsTimeEditSec.text = null

                binding.rebsWncLayoutInput.visibility = View.GONE
                binding.rebsCountLayoutInput.visibility = View.GONE
                binding.rebsTimeLayoutInput.visibility = View.GONE

                binding.rebsWncRecyclerSet.visibility = View.VISIBLE
                binding.rebsCountRecyclerSet.visibility = View.VISIBLE
                binding.rebsTimeRecyclerSet.visibility = View.VISIBLE
            }
        }

    }



    fun initResult() {
        when (setOptionKind) {
            SetOptions.WNC -> {
//                if ((TextUtils.isEmpty(binding.rebsWncEditWeight.text.toString())
//                    || TextUtils.isEmpty(binding.rebsWncEditCount.text.toString())) && !binding.rebsSwitch.isChecked) {
//                    "값을 입력해야합니다.".showShortToastSafe()
//                } else {
                    detailType.add(1)
                    when (binding.rebsSwitch.isChecked) {
                        true -> {
                            detailSetEqual.add(true)
                            val setCount = viewModel.liveDataSetCount.value.toString()
                            var wncWeight = binding.rebsWncEditWeight.text.toString()
                            var wncExCount = binding.rebsWncEditCount.text.toString()

                            if (wncWeight.isBlank()) wncWeight = "0"
                            if (wncExCount.isBlank()) wncExCount = "0"

                            detailSet.add(setCount.toInt())
                            detailTypeContext.add("$wncWeight#$wncExCount")
                            resultArrayList.add("${setCount}세트, ${wncWeight}kg, ${wncExCount}개")
                        }
                        false -> {
                            var idx = 0
                            var optionsText = ""
                            detailSetEqual.add(false)
                            viewModel.defaultAddSet()
                            val optionArrayList = viewModel.liveDataWncAddSetList.value!!
                            Log.e(optionArrayList.toString())
                            optionArrayList.forEach {
                                if (it.weight.isBlank()) it.weight = "0"
                                if (it.count.isBlank()) it.count = "0"
                                optionsText += if (idx == 0) "${it.weight}#${it.count}"
                                else "/${it.weight}#${it.count}"
                                idx++

                                resultArrayList.add("${it.setCount}, ${it.weight}kg, ${it.count}개")
                            }
                            detailSet.add(viewModel.liveDataSetCount.value!!.toInt())
                            detailTypeContext.add(optionsText)

                        }
                    }
//                }
            }
            SetOptions.COUNT -> {
//                if (TextUtils.isEmpty(binding.rebsCountEditCount.text.toString()) && !binding.rebsSwitch.isChecked) {
//                    "값을 입력해야합니다.".showShortToastSafe()
//                } else {
                    detailType.add(2)
                    when (binding.rebsSwitch.isChecked) {
                        true -> {
                            detailSetEqual.add(true)
                            val setCount = viewModel.liveDataSetCount.value.toString()
                            var countCount = binding.rebsCountEditCount.text.toString()

                            if (countCount.isBlank()) countCount = "0"

                            detailSet.add(setCount.toInt())
                            detailTypeContext.add(countCount)
                            resultArrayList.add("${setCount}세트, ${countCount}개")
                        }
                        false -> {
                            var idx = 0
                            var optionsText = ""
                            detailSetEqual.add(false)
                            viewModel.defaultAddSet()
                            val optionArrayList = viewModel.liveDataCountAddSetList.value!!
                            Log.e(optionArrayList.toString())
                            optionArrayList.forEach {
                                if (it.count.isBlank()) it.count = "0"

                                optionsText += if (idx == 0) "${it.count}"
                                else "/${it.count}"
                                idx++

                                resultArrayList.add("${it.setCount}, ${it.count}개")
                            }
                            detailSet.add(viewModel.liveDataSetCount.value!!.toInt())
                            detailTypeContext.add(optionsText)
                        }
                    }
//                }
            }
            SetOptions.TIME -> {
//                if ((TextUtils.isEmpty(binding.rebsTimeEditHour.text.toString())
//                    || TextUtils.isEmpty(binding.rebsTimeEditMin.text.toString())
//                    || TextUtils.isEmpty(binding.rebsTimeEditSec.text.toString())) && !binding.rebsSwitch.isChecked) {
//                    "값을 입력해야합니다.".showShortToastSafe()
//                } else {
                    detailType.add(3)
                    when (binding.rebsSwitch.isChecked) {
                        true -> {
                            var optionsText = ""
                            detailSetEqual.add(true)
                            val setCount = viewModel.liveDataSetCount.value.toString()
                            val timeHour = binding.rebsTimeEditHour.text.toString()
                            val timeMin = binding.rebsTimeEditMin.text.toString()
                            val timeSec = binding.rebsTimeEditSec.text.toString()
                            var timeText = ""

                            timeText += if (timeHour.isNotBlank()) "${timeHour}시간 " else "0시간 "
                            timeText += if (timeMin.isNotBlank()) "${timeMin}분 " else "0분 "
                            timeText += if (timeSec.isNotBlank()) "${timeSec}초 " else "0초 "
                            detailSet.add(setCount.toInt())
                            resultArrayList.add("${setCount}세트, $timeText")
                            optionsText = "${exchangeToSec(timeHour, timeMin, timeSec)}"
                            detailTypeContext.add(optionsText)
                        }
                        false -> {
                            var idx = 0
                            var optionsText = ""
                            detailSetEqual.add(false)
                            viewModel.defaultAddSet()
                            val optionArrayList = viewModel.liveDataTimeAddSetList.value!!
                            Log.e(optionArrayList.toString())
                            var timeText = ""

                            optionArrayList.forEach {
                                timeText += if (it.hour.isNotBlank()) "${it.hour}시간 " else "0시간 "
                                timeText += if (it.min.isNotBlank()) "${it.min}분 " else "0분 "
                                timeText += if (it.sec.isNotBlank()) "${it.sec}초 " else "0초 "

                                optionsText += if (idx == 0) "${exchangeToSec(it.hour, it.min, it.sec)}"
                                else "/${exchangeToSec(it.hour, it.min, it.sec)}"
                                idx++

                                resultArrayList.add("${it.setCount}, $timeText")
                                timeText = ""
                            }
                            detailSet.add(viewModel.liveDataSetCount.value!!.toInt())
                            detailTypeContext.add(optionsText)
                        }
                    }

//                }
            }
        }
    }

    fun exchangeToSec(hour: String, min: String, sec: String): Int {
        var exHour = 0
        var exMin = 0
        var exSec = 0
        if (hour.isNotBlank()) exHour = hour.toInt() * 3600
        if (min.isNotBlank()) exMin = min.toInt() * 60
        if (sec.isNotBlank()) exSec = sec.toInt()

        return exHour + exMin + exSec
    }

    fun setOnClickOk(exerciseName: String, clickOk: ((String, ArrayList<String>, ArrayList<Int>,ArrayList<String>, ArrayList<Boolean>, ArrayList<Int>) -> Unit)): ExerciseSetBottomSheetFragment {
        this.exerciseName = exerciseName
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        if (binding.rebsEditSetCount.text.toString() == "0") {
            "세트 수는 '1'이상으로 설정해야 합니다.".showShortToastSafe()
        } else {
            initResult()
            clickOk?.invoke(exerciseName, resultArrayList, detailType, detailTypeContext, detailSetEqual, detailSet)
            dismiss()
        }
    }
    fun onClickCancel(){
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        viewModel.defaultAddSet()
    }

}
