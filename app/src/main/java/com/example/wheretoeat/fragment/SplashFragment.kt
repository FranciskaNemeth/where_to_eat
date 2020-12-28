package com.example.wheretoeat.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wheretoeat.R
import com.example.wheretoeat.repository.Repository
import com.example.wheretoeat.viewmodel.MainViewModel
import com.example.wheretoeat.viewmodel.MainViewModelFactory

class SplashFragment : Fragment(){

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // navigating to main screen when restaurants for selected default city are received from API
        viewModel.myResponse.observe(requireActivity(), Observer { _ ->
            view?.post {
                findNavController().navigate(R.id.action_splashNav_to_mainScreenNav)
            }
        })

        viewModel.getPost("Dallas")

        // if there are errors regarding the API, an alertdialog is displayed
        viewModel.error.observe(requireActivity(), Observer { result ->
            view?.post {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle(result.toString())
                alertDialog.setMessage("Please try later!")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                            requireActivity().finish()
                        })
                alertDialog.create()
                alertDialog.show()
            }
        })

        return inflater.inflate(R.layout.splash_screen, container, false)
    }

}