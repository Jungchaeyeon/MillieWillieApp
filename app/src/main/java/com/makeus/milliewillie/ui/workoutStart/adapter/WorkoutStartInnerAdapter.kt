package com.makeus.milliewillie.ui.workoutStart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.StartRecyclerCircleItem
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartViewModel

class WorkoutStartInnerAdapter(val context: Context?, val itemList: ArrayList<StartRecyclerCircleItem>, viewModel: WorkoutStartViewModel): RecyclerView.Adapter<WorkoutStartInnerAdapter.StartInnerViewHolder>() {

    inner class StartInnerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val circleImg: ImageView = itemView.findViewById(R.id.circle_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutStartInnerAdapter.StartInnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_workout_start_recycler_inner_item, parent, false)
        return StartInnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutStartInnerAdapter.StartInnerViewHolder, position: Int) {
        val items = itemList[position]

        holder.circleImg.setImageResource(items.circle)


    }

    override fun getItemCount(): Int = itemList.size
}
