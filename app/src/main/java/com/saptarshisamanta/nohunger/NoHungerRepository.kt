package com.saptarshisamanta.nohunger

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.saptarshisamanta.nohunger.api.FoodApiService
import com.saptarshisamanta.nohunger.util.FoodPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class NoHungerRepository @Inject constructor(private val foodApiService: FoodApiService) {
    suspend fun requestSignUp(parameter:HashMap<String,String>) =
        foodApiService.requestSignUp(parameter)
    suspend fun requestLogIn(parameter: HashMap<String, String>) =
        foodApiService.requestLogIn(parameter)
    suspend fun requestDonate(
        _username: RequestBody,
        _foods: RequestBody,
        _foodType: RequestBody,
        _maxpeople: RequestBody,
        _number: RequestBody,
        _city: RequestBody,
        _address: RequestBody,
        _time: RequestBody,
        image: MultipartBody.Part
    ) = foodApiService.donateFood(_username,_foods,_foodType,_maxpeople,_number,_city,_address,_time,image)

    fun getAvailableFoods(city:String?) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
//                prefetchDistance = 0,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FoodPagingSource(foodApiService,city)
            }
        ).liveData

    suspend fun claimFood(token:String ,parameter: HashMap<String, String>)=
        foodApiService.claimFood(token,parameter)

    suspend fun yourClaimedFoods(token: String) = foodApiService.yourClaimedList(token)
    suspend fun requestLogout(token: String) = foodApiService.logOut(token)
}