package com.example.wheretoeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.model.Restaurant

class FavoriteRestaurantsRecyclerViewAdapter (dataSet: MutableList<FavoriteRestaurantsEntity>, private val clickListener: OnFavoriteRestaurantItemClickListener, private val deleteClickListener: OnFavoriteRestaurantDeleteClickListener
) : RecyclerView.Adapter<FavoriteRestaurantsRecyclerViewAdapter.ViewHolder>(){

    private val dataList: MutableList<FavoriteRestaurantsEntity>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.restaurants_listitem, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.image.context).load(dataList[position].image_url)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.image)
        holder.restaurantName.setText(dataList[position].name)
        holder.price.setText(dataList[position].price.toString())
        holder.address.setText(dataList[position].address)
        holder.favourite.setImageResource(R.drawable.favourite)
        holder.favourite.setTag(R.drawable.favourite)
        holder.favourite.setOnClickListener {
            deleteClickListener.onFavoriteRestaurantDeleteClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        var image: ImageView = itemView.findViewById(R.id.base_avatar)
        var restaurantName: TextView = itemView.findViewById(R.id.textViewTitle)
        var parenLayout: ConstraintLayout = itemView.findViewById(R.id.restaurant_view_layout)
        var price: TextView = itemView.findViewById(R.id.textViewPrice)
        var address: TextView = itemView.findViewById(R.id.textViewAddress)
        var favourite: ImageButton = itemView.findViewById(R.id.imageButton)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            clickListener.onItemClick(adapterPosition)

        }
    }

    init {
        this.dataList = dataSet
    }

}

interface OnFavoriteRestaurantItemClickListener{
    fun onItemClick(position: Int)
}

interface OnFavoriteRestaurantDeleteClickListener{
    fun onFavoriteRestaurantDeleteClick(position: Int)
}