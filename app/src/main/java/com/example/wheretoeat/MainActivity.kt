package com.example.wheretoeat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var navController : NavController
    lateinit var bottomNavView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.fragment)
        bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)

        bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainScreenNav -> showBottomNav()
                else -> hideBottomNav()
            }
        }

    }

    private fun showBottomNav() {
        bottomNavView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavView.visibility = View.GONE

    }

}