package com.saptarshisamanta.nohunger.ui.menu

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saptarshisamanta.nohunger.NoHungerRepository
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.data.Res
import kotlinx.coroutines.*
import java.io.IOException

class MenuFragmentViewModel @ViewModelInject constructor(private val repository: NoHungerRepository) :
    ViewModel() {
    var resultLogout = MutableLiveData<Resource<Res>>()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun logOutCall(token:String){
        resultLogout.value = Resource.Loading()
        uiScope.launch {
            resultLogout.value = logOutRequest(token)
        }
    }
    private suspend fun logOutRequest(token: String): Resource<Res> {
        return withContext(Dispatchers.IO){
            try{
                val response = repository.requestLogout(token)
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

}