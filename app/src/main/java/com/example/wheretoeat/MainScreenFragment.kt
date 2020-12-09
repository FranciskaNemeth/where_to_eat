package com.example.wheretoeat

import android.content.ClipData
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretoeat.model.Restaurant
import com.example.wheretoeat.repository.Repository

class MainScreenFragment : Fragment(), MainScreenRecyclerViewAdapter.OnItemClickListener {
    val dataSet = arrayListOf("alma", "korte", "csoki", "banan", "helloka")

    // retrofit test variable
    private lateinit var viewModel: MainViewModel

    var displayList : MutableList<Restaurant> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // retrofit test
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
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
        val adapter = MainScreenRecyclerViewAdapter(displayList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.citiesResponse.observe(viewLifecycleOwner, { response ->

        })

        viewModel.filteredList.observe(requireActivity(), Observer { response ->
            displayList.clear()
            displayList.addAll(response)
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        /*viewModel.myResponse.observe(requireActivity(), Observer { response ->
            displayList = response.restaurants
            recyclerView.adapter!!.notifyDataSetChanged()
        })*/

        //viewModel.getPost("London")

        val itemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        val drawable = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x7373730, -0x7373730)
        )
        drawable.setSize(1, 5)
        itemDecoration.setDrawable(drawable)
        recyclerView.addItemDecoration(itemDecoration)
    }

    override fun onItemClick(position: Int) {
        val clickedItem : String = dataSet[position]
        //adapter.notifyItemChanged(position)
    }

}