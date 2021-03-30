package com.makeus.milliewillie.ui.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.PhotoSelectedItems
import com.makeus.milliewillie.ui.SampleToast

class PhotoSelectAdapter(val context: Context?, var itemList: ArrayList<PhotoSelectedItems>): RecyclerView.Adapter<PhotoSelectAdapter.ItemViewHolder>() {

    interface MyUploadItemClickListener {
        fun onItemClick(position: Int)
    }
    private lateinit var mItemClickListener: MyUploadItemClickListener

    fun setMyUploadItemClickListener(itemClickListener: MyUploadItemClickListener){
        mItemClickListener = itemClickListener
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(adapterPosition)
            }
        }

        val image: ImageView = itemView.findViewById(R.id.photo_item_img)
        val layout: LinearLayout = itemView.findViewById(R.id.photo_item_image_layout)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoSelectAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_photo_recycler_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoSelectAdapter.ItemViewHolder, position: Int) {
        val items = itemList[position]

        when (items.isCheck) {
            true -> holder.layout.setBackgroundResource(R.drawable.photo_selected_background)
            false -> holder.layout.setBackgroundResource(R.drawable.photo_unselected_background)
        }

        Glide.with(holder.image).load(items.uri).into(holder.image)
    }



    override fun getItemCount(): Int = itemList.size
}