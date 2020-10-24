package com.saptarshisamanta.nohunger.api

import com.saptarshisamanta.nohunger.data.Food
import com.saptarshisamanta.nohunger.data.Res
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface FoodApiService {

    @POST("userroutes/signup")
    suspend fun requestSignUp(@Body field: HashMap<String, String>): Response<Res>

    @POST("userroutes/login")
    suspend fun requestLogIn(@Body field: HashMap<String, String>): Response<Res>

    @Multipart
    @POST("userroutes/donatefood")
    suspend fun donateFood(
        @Part("posted_by") username: RequestBody,
        @Part("items") foods: RequestBody,
        @Part("food_type") food_type: RequestBody,
        @Part("max_people") max_people: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("city") city: RequestBody,
        @Part("address") address: RequestBody,
        @Part("hour") hour: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Res>

    @GET("userroutes/avaiablefoodpaginate")
    suspend fun availableFoods(
        @Query("city") city: String?,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<List<Food>>

    @PATCH("userroutes/auth/claimfood")
    suspend fun claimFood(
        @Header("authorization") token: String,
        @Body field: HashMap<String, String>
    ): Response<Res>

    @GET("userroutes/auth/claimedlist")
    suspend fun yourClaimedList(
        @Header("authorization") token: String
    ): Response<List<Food>>

    @POST("userroutes/auth/logout")
    suspend fun logOut(
        @Header("authorization") token: String
    ): Response<Res>

}