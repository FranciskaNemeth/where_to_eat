package com.example.wheretoeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class SplashFragment : Fragment(){
    //override val coroutineContext: CoroutineContext
      //  get() = Dispatchers.Main + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //findNavController().navigate(R.id.action_splashNav_to_mainScreenNav)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //findNavController().navigate(R.id.action_splashNav_to_mainScreenNav)


        Timer().schedule(object : TimerTask() {
            override fun run() {
                view?.post {
                    findNavController().navigate(R.id.action_splashNav_to_mainScreenNav)
                }
            }
        }, 2000)

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