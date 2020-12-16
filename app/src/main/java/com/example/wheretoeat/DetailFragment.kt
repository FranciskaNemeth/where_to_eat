package com.example.wheretoeat

import android.content.Intent
import android.os.Bundle
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
import com.example.wheretoeat.repository.Repository

class DetailFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel =
                ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
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
            startActivity(intent)
        }

        val addOrDeleteButton : ImageButton = view.findViewById(R.id.imageButtonAddPhoto)
        addOrDeleteButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detailNav_to_addOrDeletePhotosNav)
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