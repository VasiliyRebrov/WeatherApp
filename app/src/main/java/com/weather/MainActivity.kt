package com.weather

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.weather.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels {
        ViewModelFactory("MainViewModel", application)
    }
    private val host: NavHostFragment
            by lazy {
                supportFragmentManager
                    .findFragmentById(R.id.frag_main_nav_host)
                        as NavHostFragment
            }
    private val navController by lazy { host.navController }
    private var currentDestinationId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    private fun initNav() {
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            currentDestinationId = destination.id
//        }
//    }
}
