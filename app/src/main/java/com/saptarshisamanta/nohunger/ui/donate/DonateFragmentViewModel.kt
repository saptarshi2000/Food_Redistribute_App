package com.saptarshisamanta.nohunger.ui.donate

import android.icu.text.SimpleDateFormat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saptarshisamanta.nohunger.NoHungerRepository
import com.saptarshisamanta.nohunger.api.Resource
import com.saptarshisamanta.nohunger.data.Res
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import java.util.*

class DonateFragmentViewModel @ViewModelInject constructor(private val repository: NoHungerRepository):ViewModel(){
    val result: MutableLiveData<Resource<Res>> = MutableLiveData()

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun donateCall(
        byteArray: ByteArray?,
        username: String,
        number: String,
        city: String,
        address: String,
        foods: String,
        time: String,
        foodType: String,
        maxpeople: String
    ) {
        result.value = Resource.Loading()
        val today = Date()
        val format = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss")
        val dateToStr: String = format.format(today).trim()
        val requestFile = RequestBody.create(MediaType.parse("image/png"),byteArray!!)
        val image = MultipartBody.Part.createFormData("image","$dateToStr.png",requestFile)
        val _username  = RequestBody.create(MediaType.parse("multipart/form-data"),username)
        val _number = RequestBody.create(MediaType.parse("multipart/form-data"),number)
        val _city= RequestBody.create(MediaType.parse("multipart/form-data"),city)
        val _address = RequestBody.create(MediaType.parse("multipart/form-data"),address)
        val _foods = RequestBody.create(MediaType.parse("multipart/form-data"),foods)
        val _time = RequestBody.create(MediaType.parse("multipart/form-data"),time)
        val _foodType = RequestBody.create(MediaType.parse("multipart/form-data"),foodType)
        val _maxpeople = RequestBody.create(MediaType.parse("multipart/form-data"),maxpeople)

        uiScope.launch {
            result.value = requestDonate(image,_username,_number,_city,_address,_foods,_time,_foodType,_maxpeople)
        }
    }

    private suspend fun requestDonate(
        image: MultipartBody.Part,
        _username: RequestBody,
        _number: RequestBody,
        _city: RequestBody,
        _address: RequestBody,
        _foods: RequestBody,
        _time: RequestBody,
        _foodType: RequestBody,
        _maxpeople: RequestBody
    ):Resource<Res>{
        return withContext(Dispatchers.IO){
            try {
                val response = repository.requestDonate(_username,_foods,_foodType,_maxpeople,_number,_city,_address,_time,image)
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