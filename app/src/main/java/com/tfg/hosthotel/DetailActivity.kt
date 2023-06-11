package com.tfg.hosthotel

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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

        binding.fabAdd.setOnClickListener {
            showReviewDialog()
        }
    }

    private fun showReviewDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_review, null)
        builder.setView(dialogView)

        val etReview = dialogView.findViewById(R.id.et_review) as EditText
        val btnSubmit = dialogView.findViewById(R.id.btn_submit) as Button

        val dialog = builder.create()
        dialog.show()

        btnSubmit.setOnClickListener {
            val review = etReview.text.toString()
            // Aquí puedes realizar acciones con la reseña (guardarla, enviarla, etc.)

            dialog.dismiss()
        }
    }
}
