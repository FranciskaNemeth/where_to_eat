package com.example.wheretoeat

import android.app.AlertDialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretoeat.model.City
import com.example.wheretoeat.model.Restaurant
import com.example.wheretoeat.repository.Repository


class CitiesListFragment : DialogFragment(), CitiesListRecyclerViewAdapter.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    lateinit var  inf : View
    val displayList :  MutableList<String> = ArrayList()
    lateinit var recyclerView : RecyclerView

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

        viewModel.getPostCities()

        recyclerView = inf.findViewById(R.id.recyclerViewCity)

        val adapter = CitiesListRecyclerViewAdapter(displayList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.citiesResponse.observe(requireActivity(), { response ->
            displayList.clear()
            displayList.addAll(response.cities)
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        val searchView : androidx.appcompat.widget.SearchView = inf.findViewById(R.id.searchViewCity)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
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


        val itemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        val drawable = GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x7373730, -0x7373730)
        )
        drawable.setSize(1, 5)
        itemDecoration.setDrawable(drawable)
        recyclerView.addItemDecoration(itemDecoration)
    }


    override fun onItemClick(position: Int) {
        dialog?.dismiss()
    }
}