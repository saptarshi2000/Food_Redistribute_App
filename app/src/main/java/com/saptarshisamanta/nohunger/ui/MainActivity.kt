package com.saptarshisamanta.nohunger.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.saptarshisamanta.nohunger.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
//        bottom_navBar.setupWithNavController(navHostFragment.navController)
        bottom_navBar.setupWithNavController(mainNavHostFragment.findNavController())
//        setSupportActionBar(topAppBar)
    }
}