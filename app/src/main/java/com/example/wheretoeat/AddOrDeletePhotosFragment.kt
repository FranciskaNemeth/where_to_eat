package com.example.wheretoeat

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretoeat.data.RestaurantImageEntity
import com.example.wheretoeat.repository.Repository
import com.example.wheretoeat.viewmodel.MyDatabaseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException


class AddOrDeletePhotosFragment : Fragment(), OnImageDeleteClickListener {
    val dataSet : ArrayList<ByteArray?> = ArrayList()
    lateinit var recyclerView: RecyclerView

    private lateinit var myDatabaseViewModel: MyDatabaseViewModel
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        myDatabaseViewModel = ViewModelProvider(this).get(MyDatabaseViewModel::class.java)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_or_delete_photos_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val addButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButtonAdd)
        addButton.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    2000
                )
            }
            else {
                startGallery();
            }
        }

        val adapter = AddOrDeletePhotosRecyclerViewAdapter(dataSet, this)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val itemDecoration = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )

        myDatabaseViewModel.restaurantImages.observe(viewLifecycleOwner, Observer { restaurantList ->
            dataSet.clear()
            restaurantList.forEach { restaurantImageEntity ->
                dataSet.add(restaurantImageEntity.imageData)
            }
            recyclerView.adapter!!.notifyDataSetChanged()
        })

        myDatabaseViewModel.getRestaurantImages(viewModel.selectedRestaurant.value!!.id)

        val drawable = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(-0x7373730, -0x7373730)
        )
        drawable.setSize(1, 5)
        itemDecoration.setDrawable(drawable)
        recyclerView.addItemDecoration(itemDecoration)
    }

    private fun startGallery() {
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                val returnUri: Uri? = data.data
                val bitmapImage =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, returnUri)

                val imageByteArray = convertBitmapToByteArray(bitmapImage)

                val selectedRestaurant = viewModel.selectedRestaurant.value

                if(imageByteArray != null && selectedRestaurant != null) { // selectedrestaurant should not be null on this fragment, EVER
                    val scope = CoroutineScope(Dispatchers.IO)
                    scope.launch {
                        val rid = myDatabaseViewModel.getNextPictureId()
                        Log.d("rid", "$rid")
                        val restaurantImageEntity =
                            RestaurantImageEntity(rid, selectedRestaurant.id, imageByteArray)
                        myDatabaseViewModel.addRestaurantImage(restaurantImageEntity)

                        // bevarjuk amig az uj kep, be nem szurodik az adatbazisba
                        while (rid == myDatabaseViewModel.getNextPictureId()) {
                            Thread.sleep(100)
                        }
                        myDatabaseViewModel.getRestaurantImages(selectedRestaurant.id)
                    }
                }
            }
        }
    }

    override fun onImageDeleteClick(position: Int) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val deleteImageId = myDatabaseViewModel.restaurantImages.value!![position].id
            val rid = myDatabaseViewModel.restaurantImages.value!![position].rid

            myDatabaseViewModel.deleteRestaurantImageId(deleteImageId, rid)
        }
    }

    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray? {
        var baos: ByteArrayOutputStream? = null
        return try {
            baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 35, baos)
            baos.toByteArray()
        } finally {
            if (baos != null) {
                try {
                    baos.close()
                } catch (e: IOException) { }
            }
        }
    }
}