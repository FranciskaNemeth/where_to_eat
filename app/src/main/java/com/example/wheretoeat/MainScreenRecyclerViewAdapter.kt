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
import androidx.navigation.Navigation
import com.example.wheretoeat.model.Restaurant

class MainScreenRecyclerViewAdapter(dataSet: MutableList<Restaurant>
) : RecyclerView.Adapter<MainScreenRecyclerViewAdapter.ViewHolder>(){

    private val dataList: MutableList<Restaurant>

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
        holder.restaurantName.setText(dataList[position].name)
        holder.price.setText(dataList[position].price.toString())
        holder.address.setText(dataList[position].address)
        holder.favourite.setOnClickListener {}
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
            Navigation.findNavController(itemView).navigate(R.id.action_mainScreenNav_to_detailNav)
        }

        /*override fun onClick(v: View?) {
            val position = adapterPosition
            if( position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }*/
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    init {
        this.dataList = dataSet
    }

}