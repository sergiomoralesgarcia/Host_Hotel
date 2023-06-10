package com.tfg.hosthotel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class EditActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var hotelName: String
    private var onHotelUpdatedListener: OnHotelUpdatedListener? = null

    interface OnHotelUpdatedListener : Serializable {
        fun onHotelUpdated()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        db = FirebaseFirestore.getInstance()

        val editTextName = findViewById<EditText>(R.id.edtNombre)
        val editTextCity = findViewById<EditText>(R.id.edtCiudad)
        val editTextStreet = findViewById<EditText>(R.id.edtCalle)
        val editTextInfo = findViewById<EditText>(R.id.edtInfo)
        val editTextImageUrl = findViewById<EditText>(R.id.edtUrl)

        val buttonEdit = findViewById<Button>(R.id.btnEditar)

        // Obtén el nombre del hotel de la intención
        hotelName = intent.getStringExtra("hotelName") ?: ""

        // Obtén la instancia de OnHotelUpdatedListener del intent
        onHotelUpdatedListener = intent.getSerializableExtra("onHotelUpdatedListener") as? OnHotelUpdatedListener

        // Recupera los datos actuales del hotel desde Firestore Database
        getHotelData()

        buttonEdit.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val city = editTextCity.text.toString().trim()
            val street = editTextStreet.text.toString().trim()
            val info = editTextInfo.text.toString().trim()
            val imageUrl = editTextImageUrl.text.toString().trim()

            // Verifica que se hayan completado todos los campos
            if (name.isNotEmpty() && city.isNotEmpty() && street.isNotEmpty() && info.isNotEmpty() && imageUrl.isNotEmpty()) {
                // Actualiza los datos del hotel en Firestore Database
                updateHotelData(name, city, street, info, imageUrl)

                // Establece el resultado de la actividad como RESULT_OK
                setResult(Activity.RESULT_OK)

                // Vuelve a HomeFragment
                finish()
            }
        }
    }

    private fun getHotelData() {
        db.collection("hotels")
            .whereEqualTo("name_hotel", hotelName)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val document = snapshot.documents[0]
                    val hotel = document.toObject(Hotel::class.java)
                    hotel?.let {
                        // Rellena los campos con los datos actuales del hotel
                        findViewById<EditText>(R.id.edtNombre).setText(hotel.name_hotel)
                        findViewById<EditText>(R.id.edtCiudad).setText(hotel.localtion_hotel)
                        findViewById<EditText>(R.id.edtCalle).setText(hotel.street_hotel)
                        findViewById<EditText>(R.id.edtInfo).setText(hotel.info_hotel)
                        findViewById<EditText>(R.id.edtUrl).setText(hotel.url_img)
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }

    private fun updateHotelData(name: String, city: String, street: String, info: String, imageUrl: String) {
        val hotelsCollection = db.collection("hotels")

        hotelsCollection
            .whereEqualTo("name_hotel", hotelName)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val document = snapshot.documents[0]
                    document.reference.update(
                        mapOf(
                            "name_hotel" to name,
                            "localtion_hotel" to city,
                            "street_hotel" to street,
                            "info_hotel" to info,
                            "url_img" to imageUrl
                        )
                    )
                        .addOnSuccessListener {
                            // Actualización exitosa
                            onHotelUpdatedListener?.onHotelUpdated()
                        }
                        .addOnFailureListener { exception ->
                            // Handle failure
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
}
