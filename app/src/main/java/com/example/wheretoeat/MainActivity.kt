package com.example.wheretoeat

import android.app.ActionBar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wheretoeat.repository.Repository
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var navController : NavController
    lateinit var bottomNavView : BottomNavigationView
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

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

    // search recyclerview
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu!!.findItem(R.id.actionSearch)
        val menuItemCity = menu.findItem(R.id.actionCities)

        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        viewModel.filter(newText)
                    }
                    return true
                }
            })
        }

        if (menuItemCity != null) {
            val cityButton = menuItemCity.actionView as AppCompatImageButton
            cityButton.foreground = getDrawable(R.drawable.ic_baseline_location_city_24)
            cityButton.background.alpha = 0
            cityButton.setOnClickListener{
                showDialog(cityButton)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun showDialog(view: View) {
        view.isEnabled = false

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a city")

        val options = arrayOf("Option A", "Option B")

        builder.setItems(options) { dialog, which ->
            dialog.dismiss()

            /*when (which) {
                0 -> Toast.makeText(this, "Selected city:${options[0]}", Toast.LENGTH_LONG).show()
                1 -> Toast.makeText(this, "Selected city:${options[1]}", Toast.LENGTH_LONG).show()
            }*/

            view.isEnabled = true
        }

        builder.setOnCancelListener {
            view.isEnabled = true
        }

        builder.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}