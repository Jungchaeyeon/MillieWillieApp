package com.makeusteam.milliewillie.ui.workoutStart.adapter

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
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.model.StartRecyclerCircleItem
import com.makeusteam.milliewillie.model.StartRecyclerItem
import com.makeusteam.milliewillie.ui.workoutStart.WorkoutStartViewModel
import com.makeusteam.milliewillie.util.Log

class WorkoutStartAdapter(val context: Context?, val itemList: ArrayList<StartRecyclerItem>, val viewModel: WorkoutStartViewModel): RecyclerView.Adapter<WorkoutStartAdapter.StartViewHolder>() {
    interface StartItemClickListener {
        fun onItemClick(position: Int)
    }
    private lateinit var mItemClickListener: StartItemClickListener

    fun setStartItemClickListener(itemClickListener: StartItemClickListener){
        mItemClickListener = itemClickListener
    }

    inner class StartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(adapterPosition)
            }
        }
        val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.start_item_recycler)
        val exerciseName: TextView = itemView.findViewById(R.id.start_item_text_ex_name)
        val exerciseInfo: TextView = itemView.findViewById(R.id.start_item_text_ex_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutStartAdapter.StartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_workout_start_recycler_item, parent, false)
        return StartViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutStartAdapter.StartViewHolder, position: Int) {
        val items = itemList[position]
//        statusList = ArrayList()

        holder.itemView.setOnClickListener {
//            onItemClickAddCircle(items, holder)
            if (items.stack < items.circleList.size) {
                items.stack++
                Handler(Looper.getMainLooper()).postDelayed({
                    for (i in 0 until items.stack) {
                        items.circleList[i].circle = R.drawable.one_currnet_blue
                    }
                    if (items.stack == items.circleList.size) {
                        holder.exerciseName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        holder.exerciseName.setTextColor(Color.parseColor("#9d9d9d"))
                    }
                    notifyDataSetChanged()
                }, 200)
            }
        }

        holder.innerRecyclerView.setOnClickListener {
            if (items.stack < items.circleList.size) {
                items.stack++
                Handler(Looper.getMainLooper()).postDelayed({
                    for (i in 0 until items.stack) {
                        items.circleList[i].circle = R.drawable.one_currnet_blue
                    }
                    if (items.stack == items.circleList.size) {
                        holder.exerciseName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        holder.exerciseName.setTextColor(Color.parseColor("#9d9d9d"))
                    }
                    notifyDataSetChanged()
                }, 200)
            }
        }


        holder.exerciseName.text = items.exName
        holder.exerciseInfo.text = items.exInfo

        setItemsRecycler(holder.innerRecyclerView, items.circleList, viewModel)
    }

    //inner ?????????????????? ????????? ??????
    private fun setItemsRecycler(recyclerView: RecyclerView, item: ArrayList<StartRecyclerCircleItem>, viewModel: WorkoutStartViewModel){
        val itemsAdapter = WorkoutStartInnerAdapter(context, item, viewModel)

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 10, GridLayoutManager.VERTICAL, false)
            recyclerView.adapter = itemsAdapter
        }
    }

    // on click circle count up
    private fun onItemClickAddCircle(items: StartRecyclerItem, holder: WorkoutStartAdapter.StartViewHolder) {
        Log.e("${holder.adapterPosition}")
        Log.e("stack = ${items.stack}")
        Log.e("size = ${items.circleList.size}")
        if (items.stack < items.circleList.size) {
            items.stack++

            val tempList = ArrayList<StartRecyclerCircleItem>()

            for (i in 0 until items.circleList.size) {
                tempList.add(StartRecyclerCircleItem(circle = R.drawable.one_currnet_gray))
            }



//            Handler(Looper.getMainLooper()).postDelayed({
//                for (i in 0 until items.stack) {
//                    items.circleList[i].circle = R.drawable.one_currnet_blue
//                }
//                Log.e("stack == size => ${items.stack == items.circleList.size}")
//                if (items.stack == items.circleList.size) {
//                    holder.exerciseName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
//                    holder.exerciseName.setTextColor(Color.parseColor("#9d9d9d"))
//                }
//                notifyDataSetChanged()
//            }, 200)
        }
    }

    override fun getItemCount(): Int = itemList.size
}