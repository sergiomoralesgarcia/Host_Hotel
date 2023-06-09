package com.tfg.hosthotel

import com.tfg.hosthotel.adapters.ReviewAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tfg.hosthotel.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.hosthotel.objects.Review
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewList = mutableListOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener los datos del hotel enviados desde la actividad anterior
        val hotelImage = intent.getStringExtra("hotelImage")
        val hotelName = intent.getStringExtra("hotelName")
        val hotelCity = intent.getStringExtra("hotelCity")
        val hotelStreet = intent.getStringExtra("hotelStreet")
        val hotelInfo = intent.getStringExtra("hotelInfo")

        // Mostrar los datos del hotel en las vistas correspondientes
        Picasso.get().load(hotelImage).into(binding.imgHotel)
        binding.txtNombre.text = hotelName
        binding.txtCiudad.text = hotelCity
        binding.txtCalle.text = hotelStreet
        binding.txtInfo.text = hotelInfo

        // Configurar el RecyclerView y el adaptador de reseñas
        setupRecyclerView(hotelName)

        // Mostrar el cuadro de diálogo para agregar una reseña
        binding.fabAdd.setOnClickListener {
            showReviewDialog(hotelName)
        }
    }

    private fun showReviewDialog(hotelName: String?) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_review, null)
        builder.setView(dialogView)

        val etReview = dialogView.findViewById(R.id.et_review) as EditText
        val btnSubmit = dialogView.findViewById(R.id.btn_submit) as Button

        val dialog = builder.create()
        dialog.show()

        btnSubmit.setOnClickListener {
            val reviewText = etReview.text.toString()

            val userEmail = FirebaseAuth.getInstance().currentUser?.email
            val userName = FirebaseAuth.getInstance().currentUser?.displayName

            if (hotelName != null && userEmail != null && userName != null) {
                // Obtener la fecha actual en el formato deseado
                val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

                // Crear una nueva reseña con los datos proporcionados
                val review = Review(null, hotelName, userEmail, reviewText, currentDate)
                review.userName = userName // Asignar el nombre de usuario al campo userName
                reviewList.add(review)
                reviewAdapter.notifyDataSetChanged()

                // Guardar la reseña en Firestore
                saveReviewToFirestore(review, hotelName, userEmail) // Actualizar el método

                dialog.dismiss()
            } else {
                // No se pudo obtener el nombre del hotel, el email del usuario o el nombre de usuario
            }
        }
    }

    private fun setupRecyclerView(hotelName: String?) {
        reviewAdapter = ReviewAdapter(reviewList)
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = reviewAdapter

        if (hotelName != null) {
            // Obtener las reseñas del hotel desde Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("reviews")
                .document(hotelName)
                .collection("reviews")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    reviewList.clear()
                    for (document in querySnapshot) {
                        val review = document.toObject(Review::class.java)
                        reviewList.add(review)
                    }
                    reviewAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Log.e("Review", "Error getting reviews: $e")
                }
        }
    }

    private fun saveReviewToFirestore(review: Review, hotelName: String, userEmail: String) {
        // Guardar la reseña en Firestore en la colección correspondiente al hotel
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("reviews")
            .document(hotelName)
            .collection("reviews")

        collectionRef
            .document(userEmail) // Usar el userEmail en lugar de review.userEmail
            .set(review)
            .addOnSuccessListener {
                review.id = userEmail // Usar el userEmail en lugar de review.userEmail
                reviewAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Error al guardar la reseña
            }
    }
}
