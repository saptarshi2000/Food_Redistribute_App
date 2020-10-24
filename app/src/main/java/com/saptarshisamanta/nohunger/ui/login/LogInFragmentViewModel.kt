package com.saptarshisamanta.nohunger.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saptarshisamanta.nohunger.NoHungerRepository
import com.saptarshisamanta.nohunger.data.Res
import com.saptarshisamanta.nohunger.api.Resource
import kotlinx.coroutines.*
import java.io.IOException

class LogInFragmentViewModel @ViewModelInject constructor(private val repository: NoHungerRepository): ViewModel(){

    val result:MutableLiveData<Resource<Res>>  = MutableLiveData()

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun logInCall(parameter:HashMap<String,String>){
        result.value = Resource.Loading()
        uiScope.launch {
            result.value = logInRequest(parameter)
        }
    }
    private suspend fun logInRequest(parameter: HashMap<String, String>):Resource<Res>{
        return withContext(Dispatchers.IO){
            try{
                val response = repository.requestLogIn(parameter)
                if (response.isSuccessful){
                    Resource.Success(response.body()!!)
                }else{
                    Resource.Error(response.message())
                }
            }catch (t:Throwable){
                when(t){
                    is IOException -> Resource.Error(t.message.toString())
                    else -> Resource.Error(t.message.toString())
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}