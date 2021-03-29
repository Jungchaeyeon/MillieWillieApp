package com.makeus.milliewillie.ui.report.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.ReportInnerRecyclerItem
import com.makeus.milliewillie.model.StartRecyclerCircleItem
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartViewModel

class ReportsInnerAdapter(val context: Context?, private val itemList: ArrayList<ReportInnerRecyclerItem>): RecyclerView.Adapter<ReportsInnerAdapter.ReportsnnerViewHolder>() {

    inner class ReportsnnerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val circleImg: ImageView = itemView.findViewById(R.id.report_inner_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsInnerAdapter.ReportsnnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_report_inner_recycler_item, parent, false)
        return ReportsnnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportsInnerAdapter.ReportsnnerViewHolder, position: Int) {
        val items = itemList[position]

        holder.circleImg.setImageResource(items.circle)

    }

    override fun getItemCount(): Int = itemList.size
}
