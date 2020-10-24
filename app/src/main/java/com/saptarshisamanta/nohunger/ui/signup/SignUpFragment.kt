package com.saptarshisamanta.nohunger.ui.signup

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
import com.saptarshisamanta.nohunger.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_up.*

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {


    private val viewModel:SignUpFragmentViewModel by viewModels()

    private lateinit var selection:String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        individual.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                organisationLayout.isVisible = false
            }
        }
        organisation.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                organisationLayout.isVisible = true
            }
        }
        signUp.setOnClickListener {
            it.isEnabled = false
            handleSignUp()
        }
        viewModel.result.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    signUp.isEnabled = true
                    //Toast.makeText(context, it.data!!.message, Toast.LENGTH_SHORT).show()
                    view.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
                }
                is Resource.Loading -> {
                    (Toast.makeText(context,"loading", Toast.LENGTH_SHORT)).show()
                }
                is Resource.Error -> {
                    signUp.isEnabled = true
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    private fun handleSignUp(){
        var organization =false
        var organizationName: String? = null
        val email = emailTextLayout.editText!!.text.toString().trim()
        val password  = passwordLayout.editText!!.text.toString().trim()
        val username = userLayout.editText!!.text.toString().trim()
        if (organisationLayout.isVisible){
            organizationName = organisationLayout.editText!!.text.toString().trim()
            organization = true
        }

        validateEmail(email).let {eb ->
            validatePassword(password).let {pb->
                validateUserName(username).let {ub->
                    if (organization){
                        validateOrganization(organizationName!!).let {ob->
                            if (eb && pb && ub && ob){
                                val parameter:HashMap<String,String> = HashMap()
                                parameter["email"]= email
                                parameter["password"] = password
                                parameter["username"] = username
                                parameter["organization_name"] = organizationName
                                parameter["ac_type"] = "organization"
                                signUp(parameter)
                            }else{
                                signUp.isEnabled = true
                            }
                        }
                    }else{
                        if (eb && pb && ub){
                            val parameter:HashMap<String,String> = HashMap()
                            parameter["email"]= email
                            parameter["password"] = password
                            parameter["username"] = username
                            parameter["ac_type"] = "individual"
                            signUp(parameter)
                        }else{
                            signUp.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun signUp(parameter:HashMap<String,String>){
        viewModel.signUpCall(parameter)
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            emailTextLayout.error = "E-Mail Required"
            false
        } else if (!Constants.isEmailValid(email)) {
            emailTextLayout.error = "Enter  a Valid Email"
            false
        } else {
            emailTextLayout.error = null
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.isEmpty()) {
            passwordLayout.error = "Password Required"
            false
        } else {
            passwordLayout.error = null
            true
        }
    }

    private fun validateUserName(userName: String): Boolean {
        return if (userName.isEmpty()) {
            userLayout.error = "Username Required"
            false
        } else {
            userLayout.error = null
            true
        }
    }

    private fun validateOrganization(organization: String): Boolean {
        return if (organization.isEmpty()) {
            organisationLayout.error = "Organization Name Required"
            false
        } else{
            organisationLayout.error = null
            true
        }
    }
}