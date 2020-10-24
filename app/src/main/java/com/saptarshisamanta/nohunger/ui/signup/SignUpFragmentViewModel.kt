package com.saptarshisamanta.nohunger.ui.signup

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saptarshisamanta.nohunger.NoHungerRepository
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.data.Res
import kotlinx.coroutines.*
import java.io.IOException

class SignUpFragmentViewModel @ViewModelInject constructor(private val repository: NoHungerRepository) :
    ViewModel() {

    val result: MutableLiveData<Resource<Res>> = MutableLiveData()


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    fun signUpCall(parameter: HashMap<String, String>) {
        result.value = Resource.Loading()
        uiScope.launch {
            result.value = signUpRequest(parameter)
        }
    }

    private suspend fun signUpRequest(parameter: HashMap<String, String>): Resource<Res> {
        return withContext(Dispatchers.IO) {
            try {
                Log.e("xyz", "test")
                val response = repository.requestSignUp(parameter)
                when {
                    response.isSuccessful -> {
                        Resource.Success(response.body()!!)
                    }
                    response.code() == 409 -> {
                        Resource.Error("user already exist")
                    }
                    else -> {
                        Resource.Error("internal server error 500")
                    }
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> Resource.Error(t.message.toString())
                    else -> Resource.Error("Error")
                }
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}