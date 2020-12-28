package com.example.wheretoeat.fragment

import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretoeat.*
import com.example.wheretoeat.adapter.CitiesListRecyclerViewAdapter
import com.example.wheretoeat.adapter.OnCityItemClickListener
import com.example.wheretoeat.repository.Repository
import com.example.wheretoeat.viewmodel.MainViewModel
import com.example.wheretoeat.viewmodel.MainViewModelFactory


class CitiesListFragment : DialogFragment(), OnCityItemClickListener {

    private lateinit var viewModel: MainViewModel
    lateinit var  inf : View
    val displayList :  MutableList<String> = ArrayList()
    lateinit var recyclerView : RecyclerView
    lateinit var cityName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        inf = inflater.inflate(R.layout.cities_list, container, false)
        return inf
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

        // if there are errors regarding the API, an alertdialog is displayed
        viewModel.error.observe(requireActivity(), Observer { result ->
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
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

        viewModel.getPostCities()

        recyclerView = inf.findViewById(R.id.recyclerViewCity)

        val adapter = CitiesListRecyclerViewAdapter(displayList, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // listening for citieslist change and updating the recyclerview with the searched cities list
        viewModel.citiesFilteredList.observe(requireActivity(), { response ->
            displayList.clear()
            displayList.addAll(response)
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        val searchView : androidx.appcompat.widget.SearchView = inf.findViewById(R.id.searchViewCity)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    // filtering cities based on user search
                    viewModel.filterCities(newText)
                }
                return false
            }
        })
        searchView.setIconified(false)

        val itemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        val drawable = GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x7373730, -0x7373730)
        )
        drawable.setSize(1, 5)
        itemDecoration.setDrawable(drawable)
        recyclerView.addItemDecoration(itemDecoration)
    }

    // when clicked on an item the restaurants in the selected city are loaded
    override fun onItemClick(position: Int) {
        cityName = this.displayList[position]
        viewModel.getPost(cityName)
        dialog?.dismiss()
    }
}