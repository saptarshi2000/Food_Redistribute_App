package com.saptarshisamanta.nohunger.ui.donate

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.saptarshisamanta.nohunger.R
import com.saptarshisamanta.nohunger.adapters.ViewPagerAdapter
import com.saptarshisamanta.nohunger.api.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_donate.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class DonateFragment : Fragment(R.layout.fragment_donate) {

    private var foodType: String = "veg"
    private lateinit var uri: Uri
    private lateinit var bitmap_: Bitmap
    private var byteArray: ByteArray? = null
    private val viewModel: DonateFragmentViewModel by viewModels()
    val images = listOf(
        R.drawable.food,
        R.drawable.feedingindia,
        R.drawable.food1,
        R.drawable.food2,
        R.drawable.food3
    )

    private val adapter = ViewPagerAdapter(images)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bitmap_ = (image.drawable as BitmapDrawable).bitmap
        veg.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                foodType = "veg"
            }
        }
        nonveg.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                foodType = "nonVeg"
            }
        }
        donate.setOnClickListener {
            handleDonate()
        }
        image.setOnClickListener {
            chooseFile()
        }
        viewModel.result.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(context, it.data!!.result, Toast.LENGTH_SHORT).show()
                    usernameLayout.editText?.setText("")
                    phoneLayout.editText?.setText("")
                    cityLayout.editText?.setText("")
                    addressLayout.editText?.setText("")
                    foodsLayout.editText?.setText("")
                    expiryLayout.editText?.setText("")
                    maxpeopleLayout.editText?.setText("")
                    veg.isChecked = true
                    image.setImageResource(R.drawable.food)
                    bitmap_ = (image.drawable as BitmapDrawable).bitmap

                }
                is Resource.Loading -> {
                    (Toast.makeText(context, "loading", Toast.LENGTH_SHORT)).show()
                }
                is Resource.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

            }
        })
        viewpagger.adapter = adapter
    }

    private fun handleDonate() {
        val username = usernameLayout.editText!!.text.toString().trim()
        val number = phoneLayout.editText!!.text.toString().trim()
        val city = cityLayout.editText!!.text.toString().trim()
        val address = addressLayout.editText!!.text.toString().trim()
        val foods = foodsLayout.editText!!.text.toString()
        val time = expiryLayout.editText!!.text.toString().trim()
        val maxpeople = maxpeopleLayout.editText!!.text.toString().trim()
        val b = validateUserName(username).and(validateNumber(number)).and(validateCity(city))
            .and(validateAddress(address)).and(validateFood(foods)).and(validateTime(time))
            .and(validateMaxPeople(maxpeople))
        if (b) {

            val job = Job()
            val uiScope = CoroutineScope(Dispatchers.Main + job)
            uiScope.launch {
                byteArray = getByteArray()
                requestDonate(
                    byteArray,
                    username,
                    number,
                    city,
                    address,
                    foods,
                    time,
                    foodType,
                    maxpeople
                )
            }

        }

    }

    private fun requestDonate(
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
        viewModel.donateCall(
            byteArray,
            username,
            number,
            city,
            address,
            foods,
            time,
            foodType,
            maxpeople
        )
    }

    private fun validateUserName(username: String): Boolean {
        return if (username.isNotEmpty()) {
            usernameLayout.error = null
            true
        } else {
            usernameLayout.error = "username required"
            false
        }
    }

    private fun validateNumber(number: String): Boolean {
        return if (number.isNotEmpty()) {
            phoneLayout.error = null
            true
        } else {
            phoneLayout.error = "phone no required"
            false
        }
    }

    private fun validateCity(city: String): Boolean {
        return if (city.isNotEmpty()) {
            cityLayout.error = null
            true
        } else {
            cityLayout.error = "city required"
            false
        }
    }

    private fun validateAddress(address: String): Boolean {
        return if (address.isNotEmpty()) {
            addressLayout.error = null
            true
        } else {
            addressLayout.error = "address required"
            false
        }
    }

    private fun validateFood(food: String): Boolean {
        return if (food.isNotEmpty()) {
            foodsLayout.error = null
            true
        } else {
            foodsLayout.error = "Food required"
            false
        }
    }

    private fun validateTime(hour: String): Boolean {
        return if (hour.isNotEmpty()) {
            expiryLayout.error = null
            true
        } else {
            expiryLayout.error = "Expiry needed"
            false
        }
    }

    private fun validateMaxPeople(maxpeople: String): Boolean {
        return if (maxpeople.isNotEmpty()) {
            maxpeopleLayout.error = null
            true
        } else {
            maxpeopleLayout.error = "Maxpeople needed"
            false
        }
    }

    private fun chooseFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            uri = data.data!!

            val source: ImageDecoder.Source =
                ImageDecoder.createSource(requireContext().contentResolver, uri)
            bitmap_ = ImageDecoder.decodeBitmap(source)
            image.setImageBitmap(bitmap_)
        }
    }

    private suspend fun getByteArray(): ByteArray? {
        return withContext(Dispatchers.Default) {
            val stream = ByteArrayOutputStream()
            bitmap_.compress(Bitmap.CompressFormat.PNG, 100, stream)
            byteArray = stream.toByteArray()
            byteArray
        }
    }
}