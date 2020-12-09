package com.example.wheretoeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wheretoeat.repository.Repository
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class SplashFragment : Fragment(){
    //override val coroutineContext: CoroutineContext
      //  get() = Dispatchers.Main + Job()

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //findNavController().navigate(R.id.action_splashNav_to_mainScreenNav)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //findNavController().navigate(R.id.action_splashNav_to_mainScreenNav)

        viewModel.myResponse.observe(requireActivity(), Observer {_ ->
            view?.post {
                findNavController().navigate(R.id.action_splashNav_to_mainScreenNav)
            }
        })

        viewModel.getPost("London")


        /*Timer().schedule(object : TimerTask() {
            override fun run() {
                view?.post {
                    findNavController().navigate(R.id.action_splashNav_to_mainScreenNav)
                }
            }
        }, 2000)*/

        return inflater.inflate(R.layout.splash_screen, container, false)
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch {
            delay(3000)
            withContext(Dispatchers.Main){
                findNavController().navigate(R.id.action_splashNav_to_mainScreenNav)
            }
        }
    }*/

}