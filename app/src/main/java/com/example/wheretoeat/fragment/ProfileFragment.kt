package com.example.wheretoeat.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.wheretoeat.R
import com.example.wheretoeat.entity.UserEntity
import com.example.wheretoeat.viewmodel.MyDatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.IOException


class ProfileFragment : Fragment() {
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel
    private lateinit var userNameEditText : EditText
    private lateinit var addressEditText : EditText
    private lateinit var phoneEditText : EditText
    private lateinit var emailEditText : EditText
    private lateinit var profilePic : ImageView
    private lateinit var profileImageByteArray: ByteArray
    private lateinit var favRestaurantsList : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().invalidateOptionsMenu()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.profile_screen, container, false)

        myDatabaseViewModel = ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)

        userNameEditText = view.findViewById(R.id.textInputEditTextName)
        addressEditText = view.findViewById(R.id.textInputEditTextAddress)
        phoneEditText = view.findViewById(R.id.textInputEditTextPhone)
        emailEditText = view.findViewById(R.id.textInputEditTextEmail)
        profilePic = view.findViewById(R.id.imageViewProfilePic)
        favRestaurantsList = view.findViewById(R.id.textViewFavRestaurants)

        myDatabaseViewModel.readUserData.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                view.post {
                    userNameEditText.setText(user.userName)
                    addressEditText.setText(user.address)
                    phoneEditText.setText(user.phoneNumber)
                    emailEditText.setText(user.email)
                    Glide.with(profilePic.context).load(user.imageData)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(profilePic)
                }
            }
        })

        myDatabaseViewModel.favoriteRestaurantsList.observe(requireActivity(), Observer { restaurantList ->
            view?.post {
                var favoriteRestaurantsTextFieldData = ""

                restaurantList.forEach { favRestEntity ->
                    favoriteRestaurantsTextFieldData += favRestEntity.name + "\n"
                }
                favRestaurantsList.text = favoriteRestaurantsTextFieldData
            }
        })

        profilePic.setOnClickListener {
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
                startGallery()
            }
        }

        return view
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                val returnUri: Uri? = data.data
                val bitmapImage =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, returnUri)

                val imageByteArray = convertBitmapToByteArray(bitmapImage)

                if(imageByteArray != null) { // selectedrestaurant should not be null on this fragment, EVER
                    profileImageByteArray = imageByteArray
                    view?.post {
                        Glide.with(profilePic.context).load(imageByteArray)
                                .placeholder(R.drawable.logo)
                                .error(R.drawable.logo)
                                .into(profilePic)
                    }
                }
            }
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

    override fun onStart() {
        super.onStart()
        requireActivity().setTitle("Profile")
    }

    private fun insertUserToDatabase() {
        val userName = userNameEditText.text.toString()
        val userAddress = addressEditText.text.toString()
        val userPhone = phoneEditText.text.toString()
        val userEmail = emailEditText.text.toString()

        if (inputCheck(userName, userAddress, userPhone, userEmail)) {
            // create user object
            val user = UserEntity(1, userName, userAddress, userPhone, userEmail, profileImageByteArray)

            // add data to database
            myDatabaseViewModel.addUser(user)
            Snackbar.make(requireView(), "Profile saved!", Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_profileNav_to_mainScreenNav)
        }
        else {
            Snackbar.make(requireView(), "Please fill out all fields!", Snackbar.LENGTH_LONG).show()
        }

    }

    private fun updateUser() {
        val userName = userNameEditText.text.toString()
        val userAddress = addressEditText.text.toString()
        val userPhone = phoneEditText.text.toString()
        val userEmail = emailEditText.text.toString()

        if (inputCheck(userName, userAddress, userPhone, userEmail)) {
            // create user object
            val user = UserEntity(1, userName, userAddress, userPhone, userEmail, profileImageByteArray)

            // update data in database
            myDatabaseViewModel.updateUser(user)

            Snackbar.make(requireView(), "Profile updated!", Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_profileNav_to_mainScreenNav)
        }
        else {
            Snackbar.make(requireView(), "Please fill out all fields!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String, address: String, phone: String, email: String) : Boolean{
        if(name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            return false
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_changes_menu, menu)
        //requireActivity().menuInflater.inflate(R.menu.save_changes_menu, menu)
        val menuItem = menu.findItem(R.id.saveChanges)

        if (menuItem != null) {
            val checkButton = menuItem.actionView as AppCompatImageButton
            checkButton.foreground = requireActivity().getDrawable(R.drawable.ic_baseline_check_24)
            checkButton.background.alpha = 0
            checkButton.setOnClickListener{
                if (myDatabaseViewModel.readUserData.value == null) {
                    insertUserToDatabase()
                }
                else {
                    updateUser()
                }

            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

}