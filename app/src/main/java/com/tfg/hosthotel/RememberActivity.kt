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

// Clase que maneja la actividad de recordar contraseña
class RememberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRememberBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRememberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se obtienen los elementos de la vista
        val txtemail: TextView = findViewById(R.id.txtEmailCambio)
        val btnCambiar: Button = findViewById(R.id.btnCambiar)
        val btnCancelar: Button = findViewById(R.id.btn_atras)

        // botón "Cambiar" para enviar el correo de reseteo de contraseña
        btnCambiar.setOnClickListener{
            sendPasswordReset(txtemail.text.toString())
        }

        // botón "Cancelar" para regresar a la pantalla anterior
        btnCancelar.setOnClickListener {
            onBackPressed()
        }

        // Se inicializa la instancia de Firebase Authentication
        firebaseAuth = Firebase.auth
    }

    // Función para enviar el correo de reseteo de contraseña
    private fun sendPasswordReset (email : String){

        // Se valida que el correo no esté vacío
        if (email.isEmpty()) {
            Toast.makeText(baseContext, "Rellene los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Se llama a la función de Firebase Authentication para enviar el correo de reseteo
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful){
                    // Si el proceso es exitoso, se muestra un mensaje y se regresa a la pantalla anterior
                    Toast.makeText(baseContext, "Correo de cambido de contraseña enviado", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                else{
                    // Si hay un error, se muestra un mensaje de error
                    Toast.makeText(baseContext, "Error, no se puedo completar el proceso", Toast.LENGTH_SHORT).show()

                }
            }
    }
}
