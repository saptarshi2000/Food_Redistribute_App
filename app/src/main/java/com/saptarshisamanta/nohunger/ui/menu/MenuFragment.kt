package com.saptarshisamanta.nohunger.ui.menu

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.saptarshisamanta.nohunger.R
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.di.NewSharedPreferences
import com.saptarshisamanta.nohunger.di.TokenSharedPreferences
import com.saptarshisamanta.nohunger.ui.RegisterActivity
import com.saptarshisamanta.nohunger.util.Constants
import com.saptarshisamanta.nohunger.util.Constants.Companion.DEFAULT_TOKEN
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_menu.*
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val viewModel: MenuFragmentViewModel by viewModels()
    @TokenSharedPreferences
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @NewSharedPreferences
    @Inject
    lateinit var newSharedPreferences: SharedPreferences
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        var logedin:Boolean
        val token = sharedPreferences
            .getString(KEY_TOKEN, DEFAULT_TOKEN)!!
        if (token == DEFAULT_TOKEN){
            loginlogout.text = "Login"
            logedin = false
        }else{
            loginlogout.text = "Logout"
            logedin = true
        }
        loginlogout.setOnClickListener {
            if (logedin) {
                viewModel.logOutCall(token)
            }else{
                val intent = Intent(context,RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun subscribeToLiveData(){
        viewModel.resultLogout.observe(viewLifecycleOwner,{
            when(it){
                is Resource.Success ->{
                    sharedPreferences.edit().clear().apply()
                    newSharedPreferences.edit()
                        .putBoolean(Constants.KEY_AC_FIRST_TIME,true)
                        .remove(Constants.KEY_AC_TYPE)
                        .apply()
                    val intent = Intent(context,RegisterActivity::class.java)
                    startActivity(intent)
                }
                is Resource.Loading ->{
                    Toast.makeText(context,"Loading",Toast.LENGTH_SHORT).show()
                }
                is Resource.Error ->{
                    if (it.data?.error == "auth_error"){
                        sharedPreferences.edit().clear().apply()
                        newSharedPreferences.edit()
                            .putBoolean(Constants.KEY_AC_FIRST_TIME,true)
                            .remove(Constants.KEY_AC_TYPE)
                            .apply()
                        val intent = Intent(context,RegisterActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        })
    }
}