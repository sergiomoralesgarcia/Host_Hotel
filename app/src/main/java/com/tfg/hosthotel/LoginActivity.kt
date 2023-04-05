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

        val btningresar: Button = findViewById(R.id.btnIngresar)
        val txtemail: TextView = findViewById(R.id.edtEmail)
        val txtpasswd: TextView = findViewById(R.id.edtPassword)
        val btnCrearCuentaNueva: TextView = findViewById(R.id.btnCrear)
        val btnRecordar : TextView = findViewById(R.id.btnOlvidar)

        firebaseAuth = Firebase.auth

        btningresar.setOnClickListener(){
            signIn(txtemail.text.toString(), txtpasswd.text.toString())
        }

        btnCrearCuentaNueva.setOnClickListener {
            val i = Intent (this, RegisterActivity::class.java)
            startActivity(i)
        }
        btnRecordar.setOnClickListener {
            val i = Intent (this, RememberActivity::class.java)
            startActivity(i)
        }
    }

    private fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext, "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                val verifica = user?.isEmailVerified
                if (verifica == true) {
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()

                    // accedemos a la pagina principal
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(baseContext, "Verifique su correo", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(baseContext, "Error de email y/o contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        return
    }
}