package com.tfg.hosthotel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencia a los elementos de la vista
        val btningresar: Button = findViewById(R.id.btnIngresar)
        val txtemail: TextView = findViewById(R.id.edtEmail)
        val txtpasswd: TextView = findViewById(R.id.edtPassword)
        val btnCrearCuentaNueva: TextView = findViewById(R.id.btnCrear)
        val btnRecordar : TextView = findViewById(R.id.btnOlvidar)

        // Obtenemos una instancia de FirebaseAuth
        firebaseAuth = Firebase.auth

        // botón de ingreso
        btningresar.setOnClickListener(){
            signIn(txtemail.text.toString(), txtpasswd.text.toString())
        }

        // botón de registro de cuenta nueva
        btnCrearCuentaNueva.setOnClickListener {
            val i = Intent (this, RegisterActivity::class.java)
            startActivity(i)
        }

        // botón de recordar contraseña
        btnRecordar.setOnClickListener {
            val i = Intent (this, RememberActivity::class.java)
            startActivity(i)
        }
    }

    // Función para el inicio de sesión
    private fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext, "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        // Inicio de sesión con FirebaseAuth
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                val verifica = user?.isEmailVerified
                if (verifica == true) {
                    // Si la verificación del correo electrónico es exitosa, el usuario se autentica
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()

                    // Accedemos a la página principal
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                } else {
                    // Si la verificación del correo electrónico falla, el usuario debe verificar su correo electrónico antes de autenticarse
                    Toast.makeText(baseContext, "Verifique su correo", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Si el inicio de sesión falla, se muestra un mensaje de error
                Toast.makeText(baseContext, "Error de email y/o contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Deshabilita el botón "back"
    override fun onBackPressed() {
        return
    }
}
