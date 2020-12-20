package com.example.wheretoeat.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.ImageView
import com.example.wheretoeat.R
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.entity.RestaurantImageEntity
import com.example.wheretoeat.model.Restaurant

class MainScreenRecyclerViewAdapter(dataSet: MutableList<Restaurant>,
                                    favoriteRestaurantsList : MutableList<FavoriteRestaurantsEntity>?,
                                    restaurantImageEntities:  MutableList<RestaurantImageEntity>,
                                    private val clickListener: OnRestaurantItemClickListener
) : RecyclerView.Adapter<MainScreenRecyclerViewAdapter.ViewHolder>(){

    private val dataList: MutableList<Restaurant>
    private val favorities : MutableList<FavoriteRestaurantsEntity>?
    private val restaurantImageEntities: MutableList<RestaurantImageEntity>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.restaurants_listitem, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val savedRestaurantImageData = getRestaurantImage(dataList[position].id)
        if (savedRestaurantImageData == null) {
            Glide.with(holder.image.context).load(dataList[position].image_url)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.image)
        }
        else {
            Glide.with(holder.image.context).load(savedRestaurantImageData)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.image)
        }

        holder.restaurantName.setText(dataList[position].name)
        holder.price.setText(dataList[position].price.toString())
        holder.address.setText(dataList[position].address)

        if (checkIsFavorite(dataList[position]) == false) {
            holder.favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            holder.favourite.setTag(R.drawable.ic_baseline_favorite_border_24)
        }
        else {
            holder.favourite.setImageResource(R.drawable.favourite)
            holder.favourite.setTag(R.drawable.favourite)
        }

        holder.favourite.setOnClickListener {
            if (holder.favourite.getTag() == R.drawable.ic_baseline_favorite_border_24) {
                holder.favourite.setImageResource(R.drawable.favourite)
                holder.favourite.setTag(R.drawable.favourite)
                clickListener.addOrRemoveFavorites(holder.adapterPosition, true)
            }
            else {
                holder.favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                holder.favourite.setTag(R.drawable.ic_baseline_favorite_border_24)
                clickListener.addOrRemoveFavorites(holder.adapterPosition, false)
            }
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
        this.favorities = favoriteRestaurantsList
        this.restaurantImageEntities = restaurantImageEntities
    }

    fun checkIsFavorite(restaurant: Restaurant) : Boolean {
        if (favorities != null) {
            favorities.forEach() {
                if (it.id == restaurant.id) {
                    return true
                }
            }
        }
        return false
    }

    fun getRestaurantImage(rid: Int): ByteArray? {
        restaurantImageEntities.forEach {
            if(it.rid == rid) {
                return it.imageData
            }
        }
        return null
    }

}

interface OnRestaurantItemClickListener{
    fun onItemClick(position: Int)

    fun addOrRemoveFavorites(position: Int, shouldAdd : Boolean)
}