package com.tfg.hosthotel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.tfg.hosthotel.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hotelImage = intent.getStringExtra("hotelImage")
        val hotelName = intent.getStringExtra("hotelName")
        val hotelCity = intent.getStringExtra("hotelCity")
        val hotelStreet = intent.getStringExtra("hotelStreet")
        val hotelInfo = intent.getStringExtra("hotelInfo")

        Picasso.get().load(hotelImage).into(binding.imgHotel)
        binding.txtNombre.text = hotelName
        binding.txtCiudad.text = hotelCity
        binding.txtCalle.text = hotelStreet
        binding.txtInfo.text = hotelInfo
    }
}
