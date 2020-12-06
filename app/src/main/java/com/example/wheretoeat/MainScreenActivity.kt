package com.example.wheretoeat

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainScreenActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.main_screen)

        val dataSet = arrayListOf("alma", "korte", "csoki", "banan", "helloka")

        val adapter = MainScreenRecyclerViewAdapter(dataSet)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val itemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        val drawable = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x7373730, -0x7373730)
        )
        drawable.setSize(1, 5)
        itemDecoration.setDrawable(drawable)
        recyclerView.addItemDecoration(itemDecoration)
    }

}