package com.example.wheretoeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import android.Manifest
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var googleMap: GoogleMap
    private val permissionDenied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.map_screen)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(mMap: GoogleMap) {
        googleMap = mMap

        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        googleMap.isMyLocationEnabled = true
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)

        val restaurantLocation : ImageButton = findViewById(R.id.imageButtonMarker)

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        val provider = locationManager.getBestProvider(criteria, true) ?: return
        val location = locationManager.getLastKnownLocation(provider)

        val coordinateLat = intent.getStringExtra("lat")
        val lat = coordinateLat!!.toDouble()
        val coordinateLng = intent.getStringExtra("lng")
        val lng = coordinateLng!!.toDouble()
        lateinit var restaurant : LatLng
        val name = intent.getStringExtra("name")

        googleMap.apply {
            restaurant = LatLng(lat, lng)
            addMarker(
                    MarkerOptions()
                            .position(restaurant)
                            .title(name)
            )
        }

        googleMap.animateCamera(
                CameraUpdateFactory
                        .newLatLngZoom(restaurant, 18f), 5000, null
        )

        restaurantLocation.setOnClickListener {
            googleMap.animateCamera(
                    CameraUpdateFactory
                            .newLatLngZoom(restaurant, 18f), 5000, null
            )

            Snackbar.make(findViewById(R.id.mapView), "Restaurant Location button clicked", Snackbar.LENGTH_LONG).show()
            //Toast.makeText(this, "Restaurant Location button clicked", Toast.LENGTH_SHORT).show()
        }

    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }


    override fun onMyLocationButtonClick(): Boolean {
        Snackbar.make(findViewById(R.id.mapView), "My Location button clicked", Snackbar.LENGTH_LONG).show()
        //Toast.makeText(this, "My Location button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Snackbar.make(findViewById(R.id.mapView), "Current location:\n${location.latitude}, ${location.longitude}", Snackbar.LENGTH_LONG).show()
        //Toast.makeText(this, "Current location: ${location.latitude}, ${location.longitude}", Toast.LENGTH_LONG).show()
    }

}