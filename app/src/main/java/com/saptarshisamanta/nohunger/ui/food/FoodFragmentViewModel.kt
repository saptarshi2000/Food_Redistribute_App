package com.saptarshisamanta.nohunger.ui.food

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.saptarshisamanta.nohunger.NoHungerRepository
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.data.Res
import kotlinx.coroutines.*
import java.io.IOException

class FoodFragmentViewModel @ViewModelInject constructor(private val repository: NoHungerRepository) :
    ViewModel() {
    val result:MutableLiveData<Resource<Res>> = MutableLiveData()
    private val currentCity = MutableLiveData<String>(null)

    val currentFoods = currentCity.switchMap {
        repository.getAvailableFoods(it).cachedIn(viewModelScope)
    }

    fun searchFoods(city: String?) {
        currentCity.value = city
    }
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun claimCall(token:String,parameter:HashMap<String,String>){
        uiScope.launch {
            result.value = claimRequest(token,parameter)
        }
    }

    private suspend fun claimRequest(token: String,parameter: HashMap<String, String>):Resource<Res>{
        return withContext(Dispatchers.IO){
            try {
                val t = "Bearer $token"
                val response = repository.claimFood(t,parameter)
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
    }
}