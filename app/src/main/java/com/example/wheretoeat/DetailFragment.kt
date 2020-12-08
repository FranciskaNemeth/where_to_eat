package com.example.wheretoeat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

class DetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            //Navigation.findNavController(view).navigate(R.id.action_detailNav_to_mapNav)
            //findNavController().navigate(R.id.action_detailNav_to_mapNav)

            val intent = Intent(context, MapActivity::class.java)
            startActivity(intent)
        }

        val addOrDeleteButton : ImageButton = view.findViewById(R.id.imageButtonAddPhoto)
        addOrDeleteButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detailNav_to_addOrDeletePhotosNav)
        }
    }
}