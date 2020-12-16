package com.example.wheretoeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CitiesListRecyclerViewAdapter(dataSet: MutableList<String>, private var clickListener: OnCityItemClickListener) : RecyclerView.Adapter<CitiesListRecyclerViewAdapter.ViewHolder>(){

    private val dataList: MutableList<String>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cities_listitem, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = dataList[position]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var cityName: TextView

        init {
            itemView.setOnClickListener(this)
            cityName = itemView.findViewById(R.id.textViewCityName)
        }

        override fun onClick(view: View) {
            clickListener.onItemClick(adapterPosition, view)
        }

    }

    init {
        this.dataList = dataSet
    }

}

interface OnCityItemClickListener{
    fun onItemClick(position: Int, view : View)
}