package com.saptarshisamanta.nohunger.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.saptarshisamanta.nohunger.R
import com.saptarshisamanta.nohunger.adapters.ViewPagerAdapter
import com.saptarshisamanta.nohunger.di.TokenSharedPreferences
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val images = listOf(
            R.drawable.food,
            R.drawable.feedingindia,
            R.drawable.food1,
            R.drawable.food2,
            R.drawable.food3
        )
        val adapter = ViewPagerAdapter(images)
        viewPager2.adapter = adapter
        donate.setOnClickListener {
            view.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDonationActivity())
        }
        setHasOptionsMenu(false)

    }

}