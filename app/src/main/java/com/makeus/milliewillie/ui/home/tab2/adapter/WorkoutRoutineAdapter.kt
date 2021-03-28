package com.makeus.milliewillie.ui.home.tab2.adapter

import android.content.Context
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.MyRoutineInfo
import com.makeus.milliewillie.model.StartRecyclerCircleItem
import com.makeus.milliewillie.model.StartRecyclerItem
import com.makeus.milliewillie.ui.home.tab2.WorkoutViewModel
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartViewModel
import com.makeus.milliewillie.ui.workoutStart.adapter.WorkoutStartAdapter
import com.makeus.milliewillie.ui.workoutStart.adapter.WorkoutStartInnerAdapter
import com.makeus.milliewillie.util.Log

class WorkoutRoutineAdapter(val context: Context?, val itemList: ArrayList<MyRoutineInfo>, val viewModel: WorkoutViewModel): RecyclerView.Adapter<WorkoutRoutineAdapter.RoutineViewHolder>() {
    interface RoutineItemClickListener {
        fun onItemClick(position: Int)
    }
    private lateinit var mItemClickListener: RoutineItemClickListener

    fun setRoutineItemClickListener(itemClickListener: RoutineItemClickListener){
        mItemClickListener = itemClickListener
    }

    inner class RoutineViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(adapterPosition)
            }
        }

        val routineName: TextView = itemView.findViewById(R.id.w_routine_text_name)
        val routineInfo: TextView = itemView.findViewById(R.id.w_routine_text_week_day)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutRoutineAdapter.RoutineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_routine_recycler_item, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutRoutineAdapter.RoutineViewHolder, position: Int) {
        val items = itemList[position]
        var textNameForm = ""


        if (items.isDoneRoutine == "true") {
            holder.routineName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            textNameForm = String.format(MyApplication.globalApplicationContext.getString(R.string.stroke, items.routineName))
        }
        else {
            textNameForm = items.routineName
        }

        holder.routineName.text = textNameForm
        holder.routineInfo.text = items.routineRepeatDay

    }

    override fun getItemCount(): Int = itemList.size
}