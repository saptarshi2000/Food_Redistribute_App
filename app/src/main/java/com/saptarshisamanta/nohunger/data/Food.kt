package com.saptarshisamanta.nohunger.data

data class Food (
    val _id: String?,
    val address: String?,
    val city: String?,
    var claimed: Boolean?,
    val expirationDate: String?,
    val food_type: String?,
    val imageUrl: String?,
    val items: String?,
    val max_people: Int?,
    val phone: String?,
    val posted_at: String?,
    val posted_by: String?,
    var claimed_by:String?
)