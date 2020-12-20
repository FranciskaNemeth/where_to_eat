package com.example.wheretoeat

import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.entity.RestaurantImageEntity
import com.example.wheretoeat.model.Restaurant
import com.example.wheretoeat.repository.Repository
import com.example.wheretoeat.viewmodel.MyDatabaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteRestaurantsFragment : Fragment(), OnFavoriteRestaurantItemClickListener, OnFavoriteRestaurantDeleteClickListener {
    // retrofit test variable
    private lateinit var viewModel: MainViewModel
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel

    // flag that signals if the recycle view has reached the end or some arbitrary end position
    var hasStartedDataRetrieval = false
    var displayList: MutableList<FavoriteRestaurantsEntity> = ArrayList()
    val restaurantImageEntities: MutableList<RestaurantImageEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // retrofit test
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel =
                ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

        myDatabaseViewModel = ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)

        requireActivity().invalidateOptionsMenu()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.favorite_restaurants_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = FavoriteRestaurantsRecyclerViewAdapter(displayList, restaurantImageEntities, this, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        myDatabaseViewModel.favoriteRestaurantsList.observe(viewLifecycleOwner, Observer { restaurantList ->
            displayList.clear()
            displayList.addAll(restaurantList)
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        myDatabaseViewModel.getFavoriteRestaurants()

        myDatabaseViewModel.restaurantImages.observe(requireActivity(), Observer {
            restaurantImageEntities.clear()
            restaurantImageEntities.addAll(it)
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        myDatabaseViewModel.getAllRestaurantImages()

        val itemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        val drawable = GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x7373730, -0x7373730)
        )
        drawable.setSize(1, 5)
        itemDecoration.setDrawable(drawable)
        recyclerView.addItemDecoration(itemDecoration)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().setTitle("Favorite Restaurants")
    }

    override fun onItemClick(position: Int) {
        val restaurant = displayList[position]
        viewModel.setSelectedFavoriteRestaurant(restaurant)
        findNavController().navigate(R.id.action_favoriteNav_to_detailNav)
    }

    override fun onFavoriteRestaurantDeleteClick(position: Int) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val deleteRestaurant = myDatabaseViewModel.favoriteRestaurantsList.value!![position]
            myDatabaseViewModel.deleteFavoriteRestaurant(deleteRestaurant)
        }
    }
}