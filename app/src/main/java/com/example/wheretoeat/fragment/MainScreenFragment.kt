package com.example.wheretoeat.fragment

import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretoeat.*
import com.example.wheretoeat.adapter.MainScreenRecyclerViewAdapter
import com.example.wheretoeat.adapter.OnRestaurantItemClickListener
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.entity.RestaurantImageEntity
import com.example.wheretoeat.model.Restaurant
import com.example.wheretoeat.repository.Repository
import com.example.wheretoeat.viewmodel.MainViewModel
import com.example.wheretoeat.viewmodel.MainViewModelFactory
import com.example.wheretoeat.viewmodel.MyDatabaseViewModel
import kotlin.collections.ArrayList

class MainScreenFragment : Fragment(), OnRestaurantItemClickListener {

    // retrofit test variable
    private lateinit var viewModel: MainViewModel
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel

    // flag that signals if the recycle view has reached the end or some arbitrary end position
    var hasStartedDataRetrieval = false
    var displayList: MutableList<Restaurant> = ArrayList()
    var favoritiesList : MutableList<FavoriteRestaurantsEntity> = ArrayList()
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
        return inflater.inflate(R.layout.main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = MainScreenRecyclerViewAdapter(displayList, favoritiesList, restaurantImageEntities, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // add listener for when the recycleview reaches the last couple of items
        // so we can start loading the next batch of data
        recyclerView.addOnScrollListener(getOnScrollListener())

        myDatabaseViewModel.getFavoriteRestaurants()

        // loading the favorite restaurant list in order to display the correct heart icon(is favorite or not)
        myDatabaseViewModel.favoriteRestaurantsList.observe(requireActivity(), Observer{ favorities ->
            favoritiesList.clear()
            favoritiesList.addAll(favorities)
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        // loading and displaying the restaurants from the API
        // if the user searched for something, the search result will come through here
        viewModel.restaurantsFilteredList.observe(requireActivity(), Observer { response ->
            displayList.clear()
            displayList.addAll(response)
            recyclerView.adapter!!.notifyDataSetChanged()
            hasStartedDataRetrieval = false
        })

        // if there are errors regarding the API, an alertdialog is displayed
        viewModel.error.observe(requireActivity(), Observer { result ->
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle(result.toString())
            alertDialog.setMessage("Please check your internet connection or try again later!")
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        requireActivity().finish()
                    })
            alertDialog.create()
            alertDialog.show()
        })

        // displaying the favorite restaurants images in the recyclerview
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
        requireActivity().setTitle("Where To Eat")
    }

    // navigating to restaurant detail screen
    override fun onItemClick(position: Int) {
        val restaurant = displayList[position]
        viewModel.setSelectedRestaurant(restaurant)
        var flag = false
        myDatabaseViewModel.favoriteRestaurantsList.value!!.forEach {
            // checking if selected restaurant is favorite restaurant in order to set the heart icon on detail screen
            if (it.id == restaurant.id) {
                viewModel.selectedFavoriteRestaurant.value = it
                flag = true
                return@forEach
            }
        }
        if (flag == false) {
            viewModel.selectedFavoriteRestaurant.value = null
        }
        findNavController().navigate(R.id.action_mainScreenNav_to_detailNav)
    }

    // adding or deleting restaurant from favorites on heart icon click
    override fun addOrRemoveFavorites(position: Int, shouldAdd: Boolean) {
        val restaurant = viewModel.convertRestaurantToFavoriteRestaurantEntity(displayList[position])
        if (shouldAdd == true) {
            viewModel.setSelectedFavoriteRestaurant(restaurant)
            myDatabaseViewModel.addFavoriteRestaurant(restaurant)
        }
        else {
            myDatabaseViewModel.deleteFavoriteRestaurant(restaurant)
        }
    }

    // listener which notifies if the recyclerview has been moved
    private fun getOnScrollListener(): RecyclerView.OnScrollListener {

        return object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                // if the recyclerview has displayed the fifth from last element
                // we start loading the next page of restaurants from API
                val endHasBeenReached = lastVisible + 5 >= totalItemCount

                if (totalItemCount > 0 && endHasBeenReached) {
                    // get the next batch of data
                    val currentPage = viewModel.myResponse.value?.page
                    val perPageItems = viewModel.myResponse.value?.per_page
                    val totalItems = viewModel.myResponse.value?.total_entries

                    if (currentPage != null && perPageItems != null && totalItems != null) {

                        // checking if there are more restaurants to be downloaded
                        if (currentPage * perPageItems < totalItems && !hasStartedDataRetrieval) {
                            hasStartedDataRetrieval = true
                            viewModel.getRestaurantsPaginated(viewModel.cityName, currentPage + 1)
                        }
                    }
                }
            }
        }
    }

    // search recyclerview
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.actionSearch)
        val menuItemCity = menu.findItem(R.id.actionCities)

        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        viewModel.filter(newText)
                    }
                    return false
                }
            })
        }

        if (menuItemCity != null) {
            val cityButton = menuItemCity.actionView as AppCompatImageButton
            cityButton.foreground = requireActivity().getDrawable(R.drawable.ic_baseline_location_city_24)
            cityButton.background.alpha = 0
            cityButton.setOnClickListener{
                val dialog = CitiesListFragment()
                dialog.show(requireActivity().supportFragmentManager, "cities_list")
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

}