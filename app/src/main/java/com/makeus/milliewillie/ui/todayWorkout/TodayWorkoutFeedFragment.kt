package com.makeus.milliewillie.ui.todayWorkout

import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.makeus.base.disposeOnDestroy
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentTodayWorkoutFeedBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.model.MyRoutineInfo
import com.makeus.milliewillie.model.TodayRoutines
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.EXERCISE_ID
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class TodayWorkoutFeedFragment: BaseDataBindingFragment<FragmentTodayWorkoutFeedBinding>(R.layout.fragment_today_workout_feed) {

    private val viewModel by viewModel<TodayWorkoutViewModel>()

    private var isEdit = false

    var position by Delegates.notNull<Int>()

    var liveDataItemImg = R.drawable.icon_arrow_gotodeep
    val allRoutineArray = ArrayList<MyRoutineInfo>()

    override fun FragmentTodayWorkoutFeedBinding.onBind() {
        vi = this@TodayWorkoutFeedFragment
        vm = viewModel

        executeGetAllRoutines()

        binding.todayFeedImgGotoBack.setOnClickListener {
            (activity as TodayWorkoutActivity).onClickViewChange(1)
        }

        binding.todayFeedRecycler.run {
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
                            this@TodayWorkoutFeedFragment.position = position
                            return false
                        }
                        return true
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                }

            })
            adapter = BaseDataBindingRecyclerViewAdapter<MyRoutineInfo>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MyRoutineInfo, WorkoutRoutineRecyclerItemBinding>(R.layout.workout_routine_recycler_item){
                        viFeed = this@TodayWorkoutFeedFragment
                        item = it
                    }
                )
        }


    }

    fun executeGetAllRoutines() {
        viewModel.apiRepository.getAllRoutines(
            SharedPreference.getSettingItem(WorkoutFragment.EXERCISE_ID)!!.toLong()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess){
                    Log.e("getAllRoutines 호출 성공")

                    it.result.asJsonArray.forEach { objects ->
                        val item = objects.asJsonObject

                        allRoutineArray.add(
                            MyRoutineInfo(
                                routineName = item.get("routineName").asString,
                                routineRepeatDay = item.get("routineRepeatDay").asString,
                                routineId = item.get("routineId").asLong
                            )
                        )
                    }
                    viewModel.createRoutineItem(allRoutineArray)
                } else {
                    Log.e("getAllRoutines 호출 실패")
                    Log.e(it.message)
                }
            }.disposeOnDestroy(this)
    }

    fun onClickEdit() {
        isEdit = !isEdit
        Log.e(isEdit.toString())
        when (isEdit) {
            true -> {
//                binding.todayFeedTextEdit.setText(R.string.complete)
                viewModel.liveDataTextEdit.postValue(R.string.complete)
                liveDataItemImg = R.drawable.icon_delete_btype
                viewModel.defaultRoutineItemList()
            }
            false -> {
//                binding.todayFeedTextEdit.setText(R.string.edit)
                viewModel.liveDataTextEdit.postValue(R.string.edit)
                liveDataItemImg = R.drawable.icon_arrow_gotodeep
                viewModel.defaultRoutineItemList()
            }
        }
    }

    fun onClickImage(status: Int) {
        when (status) {
            1 -> { // 루틴 수정 페이지

            }
            2 -> { // 루틴 삭제 API
                viewModel.apiRepository.deleteRoutine(SharedPreference.getSettingItem(EXERCISE_ID)!!.toLong())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it.isSuccess) {
                            Log.e("deleteRoutine 호출 성공")

                            viewModel.removeRoutineItem(position)
                        } else {
                            Log.e("deleteRoutine 호출 실패")
                            Log.e(it.message)
                        }
                    }.disposeOnDestroy(this)
            }
        }
    }


}