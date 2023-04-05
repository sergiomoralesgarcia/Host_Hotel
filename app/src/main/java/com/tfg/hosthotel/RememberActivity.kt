package com.tfg.hosthotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tfg.hosthotel.databinding.ActivityRememberBinding

class RememberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRememberBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRememberBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val txtemail: TextView = findViewById(R.id.txtEmailCambio)
        val btnCambiar: Button = findViewById(R.id.btnCambiar)
        val btnCancelar: Button = findViewById(R.id.btn_atras)

        btnCambiar.setOnClickListener{
            sendPasswordReset(txtemail.text.toString())
        }

        btnCancelar.setOnClickListener {
            onBackPressed()
        }

        firebaseAuth = Firebase.auth
    }

    private fun sendPasswordReset (email : String){
        if (email.isEmpty()) {
            Toast.makeText(baseContext, "Rellene los campoç", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful){
                    Toast.makeText(baseContext, "Correo de cambido de contraseña enviado", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                else{
                    Toast.makeText(baseContext, "Error, no se puedo completar el proceso", Toast.LENGTH_SHORT).show()

                }
            }
    }
}