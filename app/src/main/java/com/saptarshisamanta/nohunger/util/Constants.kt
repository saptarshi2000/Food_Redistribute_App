package com.saptarshisamanta.nohunger.util

class Constants {
    companion object{
        const val INDIVIDUAL = "individual"
        const val ORGANIZATION = "organization"
        const val DONOR  = "donor"
        const val DISTRIBUTOR = "distributor"
        const val AC_TYPE = "ac_type"
        const val KEY_AC_TYPE = "key_ac_type"
        const val KEY_AC_FIRST_TIME = "key_ac_first_time"
        const val BASE_URL = "http://192.168.0.130:3030/"
        const val TOKEN = "token"
        const val DEFAULT_TOKEN = "default_token"
        const val KEY_TOKEN="key_token"

        @JvmStatic
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        fun isEmailValid(email: String): Boolean {
            return EMAIL_REGEX.toRegex().matches(email);
        }
    }
}