package com.example.wheretoeat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.model.Restaurant
import com.example.wheretoeat.repository.Repository
import com.example.wheretoeat.viewmodel.MyDatabaseViewModel

class DetailFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel =
                ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
        myDatabaseViewModel = ViewModelProvider(this).get(MyDatabaseViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.detail_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapButton : ImageButton = view.findViewById(R.id.imageButtonMap)
        mapButton.setOnClickListener {
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra("lat", viewModel.selectedRestaurant.value?.lat.toString())
            intent.putExtra("lng", viewModel.selectedRestaurant.value?.lng.toString())
            intent.putExtra("name", viewModel.selectedRestaurant.value?.name.toString())
            startActivity(intent)
        }

        val addOrDeleteButton : ImageButton = view.findViewById(R.id.imageButtonAddPhoto)
        addOrDeleteButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detailNav_to_addOrDeletePhotosNav)
        }

        val favoriteButton : ImageButton = view.findViewById(R.id.imageButtonFavourite)
        val favRestId = viewModel.selectedFavoriteRestaurant.value?.id
        Log.d("favRestId", "$favRestId")
        if (favRestId != null ) {
            favoriteButton.setImageResource(R.drawable.favourite)
            favoriteButton.setTag(R.drawable.favourite)
        }
        else {
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            favoriteButton.setTag(R.drawable.ic_baseline_favorite_border_24)
        }
        favoriteButton.setOnClickListener {
            val selectedRestaurant : Restaurant = viewModel.selectedRestaurant.value!!
            val favRestaurant : FavoriteRestaurantsEntity = viewModel.convertRestaurantToFavoriteRestaurantEntity(selectedRestaurant)
            if (favoriteButton.getTag() == R.drawable.favourite) {
                myDatabaseViewModel.deleteFavoriteRestaurant(favRestaurant)
                favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                favoriteButton.setTag(R.drawable.ic_baseline_favorite_border_24)
            }
            else {
                myDatabaseViewModel.addFavoriteRestaurant(favRestaurant)
                favoriteButton.setImageResource(R.drawable.favourite)
                favoriteButton.setTag(R.drawable.favourite)
            }
        }

        val restaurantName = view.findViewById<TextView>(R.id.textViewTitle)
        val restaurantAddress = view.findViewById<TextView>(R.id.textViewAddress)
        val restaurantPhone = view.findViewById<TextView>(R.id.textViewPhone)
        val restaurantPrice = view.findViewById<TextView>(R.id.textViewPrice)

        viewModel.selectedRestaurant.observe(requireActivity(), Observer { restaurant ->
            restaurantName.text = restaurant.name
            restaurantAddress.text = restaurant.address
            restaurantPhone.text = restaurant.phone.take(10)
            restaurantPrice.text = restaurant.price.toString()
        })
    }
}