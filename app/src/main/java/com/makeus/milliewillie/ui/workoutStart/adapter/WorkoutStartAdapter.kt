package com.makeus.milliewillie.ui.workoutStart.adapter

import android.content.Context
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.StartRecyclerCircleItem
import com.makeus.milliewillie.model.StartRecyclerItem
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartViewModel
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception

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
            onItemClickAddCircle(items, holder)
        }

        holder.innerRecyclerView.setOnClickListener {
            onItemClickAddCircle(items, holder)
        }


        holder.exerciseName.text = items.exName
        holder.exerciseInfo.text = items.exInfo

        setItemsRecycler(holder.innerRecyclerView, items.circleList, viewModel)
    }

    //inner 리사이클러뷰 어답터 장착
    fun setItemsRecycler(recyclerView: RecyclerView, item: ArrayList<StartRecyclerCircleItem>, viewModel: WorkoutStartViewModel){
        val itemsAdapter = WorkoutStartInnerAdapter(context, item, viewModel)

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 10, GridLayoutManager.VERTICAL, false)
            recyclerView.adapter = itemsAdapter
        }
    }

    // on click circle count up
    fun onItemClickAddCircle(items: StartRecyclerItem, holder: WorkoutStartAdapter.StartViewHolder) {
        items.stack++

        val tempList = ArrayList<StartRecyclerCircleItem>()

        for (i in 0 until items.circleList.size) {
            tempList.add(StartRecyclerCircleItem(circle = R.drawable.one_currnet_gray))
        }

        Handler(Looper.getMainLooper()).postDelayed({
            for (i in 0 until items.stack) {
                items.circleList[i].circle = R.drawable.one_currnet_blue
            }

            if (items.stack == items.circleList.size) {
                holder.exerciseName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            notifyDataSetChanged()
        }, 200)
    }

    override fun getItemCount(): Int = itemList.size
}