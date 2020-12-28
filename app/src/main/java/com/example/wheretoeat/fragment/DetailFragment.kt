package com.example.wheretoeat.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.wheretoeat.viewmodel.MainViewModel
import com.example.wheretoeat.viewmodel.MainViewModelFactory
import com.example.wheretoeat.MapActivity
import com.example.wheretoeat.R
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.model.Restaurant
import com.example.wheretoeat.repository.Repository
import com.example.wheretoeat.viewmodel.MyDatabaseViewModel

class DetailFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel

    private lateinit var restaurantName : TextView
    private lateinit var restaurantAddress : TextView
    private lateinit var restaurantPhone : TextView
    private lateinit var restaurantPrice : TextView
    private lateinit var phoneButton : ImageView
    private lateinit var restaurantImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel =
                ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
        myDatabaseViewModel = ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
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

        restaurantName = view.findViewById<TextView>(R.id.textViewTitle)
        restaurantAddress = view.findViewById<TextView>(R.id.textViewAddress)
        restaurantPhone = view.findViewById<TextView>(R.id.textViewPhone)
        restaurantPrice = view.findViewById<TextView>(R.id.textViewPrice)
        phoneButton = view.findViewById(R.id.imageViewPhoneIcon)
        restaurantImageView = view.findViewById(R.id.imageView)

        phoneButton.setOnClickListener {
            if (restaurantPhone.text.toString().isNotEmpty()) {
                callRestaurant()
            }
        }

        restaurantPhone.setOnClickListener {
            if (restaurantPhone.text.toString().isNotEmpty()) {
                callRestaurant()
            }
        }

        // displaying the selected restaurant data on the fragment
        viewModel.selectedRestaurant.observe(requireActivity(), Observer { restaurant ->
            restaurantName.text = restaurant.name
            restaurantAddress.text = restaurant.address
            restaurantPhone.text = restaurant.phone.take(10)
            restaurantPrice.text = restaurant.price.toString()
        })

        // displaying the latest restaurant profile picture
        myDatabaseViewModel.restaurantImages.observe(requireActivity(), Observer { restaurantList ->
            /* displaying photo on the main thread: if there isn't any image saved in the database
            *  the image from the API will be displayed, or a placeholder */
            view.post {
                if(restaurantList.isNullOrEmpty()) {
                    Glide.with(restaurantImageView.context).load(viewModel.selectedRestaurant.value!!.image_url)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(restaurantImageView)
                }
                else {
                    Glide.with(restaurantImageView.context).load(restaurantList[0].imageData)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(restaurantImageView)
                }

            }
        })

        myDatabaseViewModel.getRestaurantImages(viewModel.selectedRestaurantId)
    }

    fun callRestaurant() {
        val phoneNumber : String = restaurantPhone.text.toString()
        if (phoneNumber.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")

            // permission request
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
            }
            else {
                startActivity(intent)
            }
        }
    }
}