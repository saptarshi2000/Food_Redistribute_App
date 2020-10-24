package com.saptarshisamanta.nohunger.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.saptarshisamanta.nohunger.R
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.di.TokenSharedPreferences
import com.saptarshisamanta.nohunger.ui.MainActivity
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_TOKEN
import com.saptarshisamanta.nohunger.util.Constants.Companion.isEmailValid
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_log_in.*
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment: Fragment(R.layout.fragment_log_in) {

    @TokenSharedPreferences
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel:LogInFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login.setOnClickListener {
            loginHandler()
        }
        signuppage.setOnClickListener { signup: View ->
            view.findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
        }
        viewModel.result.observe(viewLifecycleOwner,{
            when(it){
                is Resource.Success ->{
                    login.isEnabled = true
                    progressBar2.isVisible = false
//                    Toast.makeText(context,it.data!!.token,Toast.LENGTH_SHORT).show()
                    sharedPreferences.edit()
                        .putString(KEY_TOKEN,it.data!!.token)
                        .apply()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                is Resource.Error ->{
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                    login.isEnabled = true
                    progressBar2.isVisible =false
                }
                is Resource.Loading ->{
                    login.isEnabled = false
                    progressBar2.isVisible = true
                }

            }
        })
    }
    private fun loginHandler(){
        val email:String = emailTextLayout.editText!!.text.toString().trim()
        val password:String = passwordLayout.editText!!.text.toString().trim()

        validateEmail(email).let {
            if (validatePassword(password) && it){

                val parameter = HashMap<String,String>()
                parameter["email"] = email
                parameter["password"] = password
                viewModel.logInCall(parameter)
            }else{
                return
            }
        }
    }
    private fun validateEmail(email:String) : Boolean {
        return if (email.isEmpty()){
            emailTextLayout.error = "E-Mail Required"
            false
        }else if (!isEmailValid(email)){
            emailTextLayout.error = "Enter  a Valid Email"
            false
        }
        else{
            emailTextLayout.error = null
            true
        }
    }
    private fun validatePassword(password:String) : Boolean {
        return if (password.isEmpty()){
            passwordLayout.error = "Password Required"
            false
        }else{
            passwordLayout.error = null
            true
        }
    }
}