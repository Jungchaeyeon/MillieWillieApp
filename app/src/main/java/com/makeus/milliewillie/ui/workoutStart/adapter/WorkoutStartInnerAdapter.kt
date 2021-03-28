package com.makeus.milliewillie.ui.workoutStart.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.makeus.milliewillie.R
import com.makeus.milliewillie.ext.setImage
import com.makeus.milliewillie.model.StartRecyclerCircleItem
import com.makeus.milliewillie.ui.view.CircleIndicator
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartViewModel

class WorkoutStartInnerAdapter(val context: Context?, private val itemList: ArrayList<StartRecyclerCircleItem>, viewModel: WorkoutStartViewModel): RecyclerView.Adapter<WorkoutStartInnerAdapter.StartInnerViewHolder>() {

    inner class StartInnerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val circleImg: ImageView = itemView.findViewById(R.id.circle_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutStartInnerAdapter.StartInnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_workout_start_recycler_inner_item, parent, false)
        return StartInnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutStartInnerAdapter.StartInnerViewHolder, position: Int) {
        val items = itemList[position]
//        val items = itemList.value
        holder.circleImg.setImageResource(items.circle)

//        holder.circleImg.custom2CreateDotPanel(itemList.size,R.drawable.one_currnet_gray, R.drawable.one_currnet_blue, stackOutter)

//        holder.circleImg.apply {
//            setOnClickListener {
//                custom2CreateDotPanel(itemList.size,R.drawable.one_currnet_gray, R.drawable.one_currnet_blue, items.stack)
//                items.stack++
//            }
//        }



    }

    override fun getItemCount(): Int = itemList.size
}
