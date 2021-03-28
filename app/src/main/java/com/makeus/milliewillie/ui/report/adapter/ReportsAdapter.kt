package com.makeus.milliewillie.ui.report.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.ReportInnerRecyclerItem
import com.makeus.milliewillie.model.ReportRecyclerItem
import com.makeus.milliewillie.ui.workoutStart.adapter.WorkoutStartInnerAdapter

class ReportsAdapter(val context: Context?, val itemList: ArrayList<ReportRecyclerItem>): RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {
    interface ReportsItemClickListener {
        fun onItemClick(position: Int)
    }
    private lateinit var mItemClickListener: ReportsItemClickListener

    fun setReportsItemClickListener(itemClickListener: ReportsItemClickListener){
        mItemClickListener = itemClickListener
    }

    inner class ReportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(adapterPosition)
            }
        }
        val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.report_item_recycler)
        val exerciseName: TextView = itemView.findViewById(R.id.report_item_text_ex_name)
        val exerciseStatus: TextView = itemView.findViewById(R.id.report_text_ex_complete)
        val exerciseOption: TextView = itemView.findViewById(R.id.report_text_ex_option)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsAdapter.ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_report_recycler_item, parent, false)
        return ReportViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ReportsAdapter.ReportViewHolder, position: Int) {
        val items = itemList[position]

        holder.exerciseName.text = items.exerciseName
        holder.exerciseStatus.text = items.exerciseStatus
        holder.exerciseOption.text = items.exerciseOption

        if (items.isDone) holder.exerciseStatus.setTextColor(Color.parseColor("#00d8ff"))
        else holder.exerciseStatus.setTextColor(Color.parseColor("#bbbbbb"))


        setItemsRecycler(holder.innerRecyclerView, items.doneList)
    }

    //inner 리사이클러뷰 어답터 장착
    fun setItemsRecycler(recyclerView: RecyclerView, item: ArrayList<ReportInnerRecyclerItem>){
        val itemsAdapter = ReportsInnerAdapter(context, item)

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 10, GridLayoutManager.VERTICAL, false)
            recyclerView.adapter = itemsAdapter
        }
    }

    override fun getItemCount(): Int = itemList.size
}