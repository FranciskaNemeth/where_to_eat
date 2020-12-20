package com.example.wheretoeat

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.bumptech.glide.Glide
import android.widget.ImageView

class AddOrDeletePhotosRecyclerViewAdapter(dataSet: ArrayList<ByteArray?>, private var clickListener: OnImageDeleteClickListener) : RecyclerView.Adapter<AddOrDeletePhotosRecyclerViewAdapter.ViewHolder>(){

    private val dataList:ArrayList<ByteArray?>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.photos_listitem, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.image.context).load(dataList[position])
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.image)
        //holder.image.setImageBitmap(dataList[position])
        holder.delete.setOnClickListener {
            clickListener.onImageDeleteClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageViewPhoto)
        var delete: ImageButton = itemView.findViewById(R.id.imageButtonDeletePhoto)
    }

    init {
        this.dataList = dataSet
    }

}

interface OnImageDeleteClickListener{
    fun onImageDeleteClick(position: Int)
}