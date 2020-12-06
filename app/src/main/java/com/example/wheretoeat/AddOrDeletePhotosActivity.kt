package com.example.wheretoeat

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AddOrDeletePhotosActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.add_or_delete_photos_screen)

        val dataSet = arrayListOf("1", "2", "3", "4", "5")

        val adapter = AddOrDeletePhotosRecyclerViewAdapter(dataSet)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val itemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        val drawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x7373730, -0x7373730)
        )
        drawable.setSize(1, 5)
        itemDecoration.setDrawable(drawable)
        recyclerView.addItemDecoration(itemDecoration)
    }

}