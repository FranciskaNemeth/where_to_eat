package com.example.wheretoeat

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wheretoeat.entity.UserEntity
import com.example.wheretoeat.viewmodel.MyDatabaseViewModel
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel
    private lateinit var userNameEditText : EditText
    private lateinit var addressEditText : EditText
    private lateinit var phoneEditText : EditText
    private lateinit var emailEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().invalidateOptionsMenu()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.profile_screen, container, false)

        myDatabaseViewModel = ViewModelProvider(this).get(MyDatabaseViewModel::class.java)

        userNameEditText = view.findViewById<EditText>(R.id.textInputEditTextName)
        addressEditText = view.findViewById<EditText>(R.id.textInputEditTextAddress)
        phoneEditText = view.findViewById<EditText>(R.id.textInputEditTextPhone)
        emailEditText = view.findViewById<EditText>(R.id.textInputEditTextEmail)

        myDatabaseViewModel.readUserData.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                userNameEditText.setText(user.userName)
                addressEditText.setText( user.address)
                phoneEditText.setText(user.phoneNumber)
                emailEditText.setText(user.email)
            }

        })

        return view
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
            val user = UserEntity(1, userName, userAddress, userPhone, userEmail)

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
            val user = UserEntity(1, userName, userAddress, userPhone, userEmail)

            // update data in database
            myDatabaseViewModel.updateUser(user)

            Snackbar.make(requireView(), "Profile updated!", Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_profileNav_to_mainScreenNav)
        }
        else {
            Snackbar.make(requireView(), "Please fill out all fields!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name : String, address : String, phone : String, email : String) : Boolean{
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