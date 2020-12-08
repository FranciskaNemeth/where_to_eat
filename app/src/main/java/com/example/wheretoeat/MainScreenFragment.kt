package com.example.wheretoeat

import android.content.ClipData
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainScreenFragment : Fragment(), MainScreenRecyclerViewAdapter.OnItemClickListener {
    val dataSet = arrayListOf("alma", "korte", "csoki", "banan", "helloka")
    val adapter = MainScreenRecyclerViewAdapter(dataSet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
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
        adapter.notifyItemChanged(position)
    }
}