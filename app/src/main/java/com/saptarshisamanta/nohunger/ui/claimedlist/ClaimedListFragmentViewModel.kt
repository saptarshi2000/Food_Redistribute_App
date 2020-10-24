package com.saptarshisamanta.nohunger.ui.claimedlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saptarshisamanta.nohunger.NoHungerRepository
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.data.Food
import kotlinx.coroutines.*
import java.io.IOException

class ClaimedListFragmentViewModel @ViewModelInject constructor(private val repository: NoHungerRepository) :
    ViewModel() {
    val result = MutableLiveData<Resource<List<Food>>>()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun claimedFoodCall(token: String) {
        uiScope.launch {
            result.value = claimedFoodRequest(token)
        }
    }

    private suspend fun claimedFoodRequest(token: String): Resource<List<Food>> {
        return withContext(Dispatchers.IO) {
            try {
                val t = "Bearer $token"
                val response = repository.yourClaimedFoods(t)
                if (response.isSuccessful) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(response.message())
                }

            } catch (t: Throwable) {
                when (t) {
                    is IOException -> Resource.Error(t.message.toString())
                    else -> Resource.Error(t.message.toString())
                }
            }
        }
    }
}