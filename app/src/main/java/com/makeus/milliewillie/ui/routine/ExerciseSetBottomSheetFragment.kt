package com.makeus.milliewillie.ui.routine

import android.os.Build
import android.os.Bundle
import android.text.Editable
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments.let {
//            exerciseName = it?.get(EXERCISE_NAME).toString()
//        }
        viewModel.liveDataExerciseName.postValue(exerciseName)
        position = 0
    }

    private var clickOk: ((String, ArrayList<String>) -> Unit)? = null

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
                                viewModel.addPositionItem(SetOptions.WNC, position, weightValue, 1)
                                Log.e(viewModel.wncSetItemList.toString())
                            }
                        })
                        this.rebsWncRecyclerEditCount.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                countValue = s.toString()
                            }
                            override fun afterTextChanged(s: Editable?) {
                                viewModel.addPositionItem(SetOptions.WNC, position, countValue, 2)
                                Log.e(viewModel.wncSetItemList.toString())
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
                                viewModel.addPositionItem(SetOptions.COUNT, position, countValue, 1)
                                Log.e(viewModel.countSetItemList.toString())
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
                                viewModel.addPositionItem(SetOptions.TIME, position, hourValue, 1)
                                Log.e(viewModel.timeSetItemList.toString())
                            }
                        })
                        this.rebsTimeRecyclerEditMin.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                minValue = s.toString()
                            }
                            override fun afterTextChanged(s: Editable?) {
                                viewModel.addPositionItem(SetOptions.TIME, position, minValue, 2)
                                Log.e(viewModel.timeSetItemList.toString())
                            }
                        })
                        this.rebsTimeRecyclerEditSec.addTextChangedListener(object : TextWatcher{
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                secValue = s.toString()
                            }
                            override fun afterTextChanged(s: Editable?) {
                                viewModel.addPositionItem(SetOptions.TIME, position, secValue, 3)
                                Log.e(viewModel.timeSetItemList.toString())
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
                        Log.e("$position")
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
                viewModel.addItem()
            }
            R.id.rebs_btn_minus -> {
                viewModel.decreaseSetCount("${viewModel.liveDataSetCount.value!!.toInt() - 1}")
                viewModel.removeItem()
            }
        }

    }

    fun onClickSwitch() {
        when (binding.rebsSwitch.isChecked) {
            true -> {
                binding.rebsWncLayoutInput.visibility = View.VISIBLE
                binding.rebsCountLayoutInput.visibility = View.VISIBLE
                binding.rebsTimeLayoutInput.visibility = View.VISIBLE

                binding.rebsWncRecyclerSet.visibility = View.GONE
                binding.rebsCountRecyclerSet.visibility = View.GONE
                binding.rebsTimeRecyclerSet.visibility = View.GONE
            }
            false -> {
                binding.rebsWncLayoutInput.visibility = View.GONE
                binding.rebsCountLayoutInput.visibility = View.GONE
                binding.rebsTimeLayoutInput.visibility = View.GONE

                binding.rebsWncRecyclerSet.visibility = View.VISIBLE
                binding.rebsCountRecyclerSet.visibility = View.VISIBLE
                binding.rebsTimeRecyclerSet.visibility = View.VISIBLE
            }
        }

    }

    var resultArrayList = ArrayList<String>()

    private val detailType = ArrayList<Int>()
    private val detailTypeContext = ArrayList<String>()
    private val detailSetEqual = ArrayList<Boolean>()
    private val detailSet = ArrayList<Int>()

    fun initResult() {
        when (setOptionKind) {
            SetOptions.WNC -> {
                detailType.add(1)
                when (binding.rebsSwitch.isChecked) {
                    true -> {
                        detailSetEqual.add(true)
                        val setCount = viewModel.liveDataSetCount.value.toString()
                        val wncWeight = binding.rebsWncEditWeight.text.toString()
                        val wncExCount = binding.rebsWncEditCount.text.toString()

                        detailSet.add(setCount.toInt())
                        resultArrayList.add("${setCount}세트, ${wncWeight}kg, ${wncExCount}개")
                    }
                    false -> {
                        detailSetEqual.add(false)
                        viewModel.defaultAddSet()
                        val optionArrayList = viewModel.liveDataWncAddSetList.value!!
                        Log.e(optionArrayList.toString())
                        optionArrayList.forEach {
                            detailSet.add(it.setCount[0].toInt())
                            resultArrayList.add("${it.setCount}, ${it.weight}kg, ${it.count}개")
                        }

                    }
                }
            }
            SetOptions.COUNT -> {
                detailType.add(2)
                when (binding.rebsSwitch.isChecked) {
                    true -> {
                        detailSetEqual.add(true)
                        val setCount = viewModel.liveDataSetCount.value.toString()
                        val countCount = binding.rebsCountEditCount.text.toString()

                        detailSet.add(setCount.toInt())
                        resultArrayList.add("${setCount}세트, ${countCount}개")
                    }
                    false -> {
                        detailSetEqual.add(false)
                        viewModel.defaultAddSet()
                        val optionArrayList = viewModel.liveDataCountAddSetList.value!!
                        Log.e(optionArrayList.toString())
                        optionArrayList.forEach {
                            detailSet.add(it.setCount[0].toInt())
                            resultArrayList.add("${it.setCount}, ${it.count}개")
                        }
                    }
                }
            }
            SetOptions.TIME -> {
                detailType.add(3)
                when (binding.rebsSwitch.isChecked) {
                    true -> {
                        detailSetEqual.add(true)
                        val setCount = viewModel.liveDataSetCount.value.toString()
                        val timeHour = binding.rebsTimeEditHour.text.toString()
                        val timeMin = binding.rebsTimeEditMin.text.toString()
                        val timeSec = binding.rebsTimeEditSec.text.toString()
                        var timeText = ""

                        if (!timeHour.isBlank()) timeText += "${timeHour}시 "
                        if (!timeMin.isBlank()) timeText += "${timeMin}분 "
                        if (!timeSec.isBlank()) timeText += "${timeSec}초 "

                        detailSet.add(setCount.toInt())
                        resultArrayList.add("${setCount}세트, $timeText")
                    }
                    false -> {
                        detailSetEqual.add(false)
                        viewModel.defaultAddSet()
                        val optionArrayList = viewModel.liveDataTimeAddSetList.value!!
                        Log.e(optionArrayList.toString())
                        var timeText = ""

                        optionArrayList.forEach {
                            if (!it.hour.isBlank()) timeText += "${it.hour}시간 "
                            if (!it.min.isBlank()) timeText += "${it.min}분 "
                            if (!it.sec.isBlank()) timeText += "${it.sec}초 "

                            detailSet.add(it.setCount[0].toInt())
                            resultArrayList.add("${it.setCount}, $timeText")
                            timeText = ""
                        }
                    }
                }
            }
        }
    }

    fun setOnClickOk(exerciseName: String, clickOk: ((String, ArrayList<String>) -> Unit)): ExerciseSetBottomSheetFragment {
        this.exerciseName = exerciseName
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        initResult()
        clickOk?.invoke(exerciseName, resultArrayList)
        dismiss()
    }
    fun onClickCancel(){
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        viewModel.defaultAddSet()
    }

}
