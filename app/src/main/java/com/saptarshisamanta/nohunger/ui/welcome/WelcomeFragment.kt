package com.saptarshisamanta.nohunger.ui.welcome

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.saptarshisamanta.nohunger.R
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.di.NewSharedPreferences
import com.saptarshisamanta.nohunger.di.TokenSharedPreferences
import com.saptarshisamanta.nohunger.ui.MainActivity
import com.saptarshisamanta.nohunger.util.Constants
import com.saptarshisamanta.nohunger.util.Constants.Companion.DEFAULT_TOKEN
import com.saptarshisamanta.nohunger.util.Constants.Companion.DONOR
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_AC_FIRST_TIME
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_AC_TYPE
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_welcome.*
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    @NewSharedPreferences
    @Inject
    lateinit var sharedPref: SharedPreferences

    @TokenSharedPreferences
    @Inject
    lateinit var tokenSharedPreferences: SharedPreferences


    private val viewModel by viewModels<WelcomeFragmentViewModel>()

    private var selection = DONOR
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isFirstTime = sharedPref.getBoolean(KEY_AC_FIRST_TIME, true)
        val token = tokenSharedPreferences.getString(KEY_TOKEN, DEFAULT_TOKEN)!!
        Log.e("xyz", "test")

        if (!isFirstTime) {
            val sel = sharedPref.getString(KEY_AC_TYPE, DONOR)
            if (sel == DONOR) {
                Log.e("xyz", "error")
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                if (token != DEFAULT_TOKEN) {
                    val parameter: HashMap<String, String> = HashMap()
                    parameter["token"] = token
                    viewModel.logInCall(parameter)

                } else {
                    requireView().findNavController()
                        .navigate((WelcomeFragmentDirections.actionWelcomeFragmentToLogInFragment()))
                }
            }
        }
        donor.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                selection = DONOR
                Toast.makeText(context, DONOR, Toast.LENGTH_SHORT).show()
            }
        }
        distributor.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                selection = Constants.DISTRIBUTOR
                Toast.makeText(context, Constants.DISTRIBUTOR, Toast.LENGTH_SHORT).show()
            }
        }
        next.setOnClickListener {
            sharedPref.edit()
                .putString(KEY_AC_TYPE, selection)
                .putBoolean(KEY_AC_FIRST_TIME, false)
                .apply()
            if (selection == DONOR) {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            } else {
                view.findNavController().navigate(
                    WelcomeFragmentDirections.actionWelcomeFragmentToLogInFragment()
                )
            }
        }
        viewModel.result.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Login again", Toast.LENGTH_SHORT).show()
                    requireView().findNavController()
                        .navigate((WelcomeFragmentDirections.actionWelcomeFragmentToLogInFragment()))
                }
                is Resource.Loading -> {
                    Toast.makeText(context, "loading", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }


}