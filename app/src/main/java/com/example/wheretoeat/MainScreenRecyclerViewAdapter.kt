package com.example.wheretoeat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.bumptech.glide.Glide
import android.widget.ImageView

class MainScreenRecyclerViewAdapter(dataSet: ArrayList<String>) : RecyclerView.Adapter<MainScreenRecyclerViewAdapter.ViewHolder>(){

    private val dataList: ArrayList<String>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.restaurants_listitem, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.image.context).load("https://demjencascade.hu/site/uploads/2020/05/110719-cc-ss-christmas-presents-generic-img.jpg")
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.image)
        holder.restaurantName.setText(dataList[position])
        holder.price.setText(dataList[position])
        holder.address.setText(dataList[position])
        holder.favourite.setOnClickListener {}
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var restaurantName: TextView
        var parenLayout: ConstraintLayout
        var price: TextView
        var address: TextView
        var favourite: ImageButton

        init {
            image = itemView.findViewById(R.id.base_avatar)
            restaurantName = itemView.findViewById(R.id.textViewTitle)
            parenLayout = itemView.findViewById(R.id.restaurant_view_layout)
            price = itemView.findViewById(R.id.textViewPrice)
            address = itemView.findViewById(R.id.textViewAddress)
            favourite = itemView.findViewById(R.id.imageButton)
        }
    }

    init {
        this.dataList = dataSet
    }

}