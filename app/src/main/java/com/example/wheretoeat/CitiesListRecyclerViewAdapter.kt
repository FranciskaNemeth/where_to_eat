package com.example.wheretoeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretoeat.model.City
import java.util.ArrayList

class CitiesListRecyclerViewAdapter(dataSet: MutableList<String>) : RecyclerView.Adapter<CitiesListRecyclerViewAdapter.ViewHolder>(){

    private val dataList: MutableList<String>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.photos_listitem, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = dataList[position]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityName: TextView


        init {
            cityName = itemView.findViewById(R.id.imageViewPhoto)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    init {
        this.dataList = dataSet
    }

}