package com.example.wheretoeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.bumptech.glide.Glide
import android.widget.ImageView

class AddOrDeletePhotosRecyclerViewAdapter(dataSet: ArrayList<String>) : RecyclerView.Adapter<AddOrDeletePhotosRecyclerViewAdapter.ViewHolder>(){

    private val dataList: ArrayList<String>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.photos_listitem, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.image.context).load("https://demjencascade.hu/site/uploads/2020/05/110719-cc-ss-christmas-presents-generic-img.jpg")
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.image)
        holder.delete.setOnClickListener {}
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var delete: ImageButton

        init {
            image = itemView.findViewById(R.id.imageViewPhoto)
            delete = itemView.findViewById(R.id.imageButtonDeletePhoto)
        }
    }

    init {
        this.dataList = dataSet
    }

}