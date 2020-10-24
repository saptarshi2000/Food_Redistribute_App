package com.saptarshisamanta.nohunger.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.saptarshisamanta.nohunger.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}