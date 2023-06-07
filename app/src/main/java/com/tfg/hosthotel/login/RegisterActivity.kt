package com.tfg.hosthotel.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tfg.hosthotel.R
import java.util.jar.Attributes.Name

class RegisterActivity : AppCompatActivity() {
    // Declaración de la instancia de FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        // Declaración de los elementos del layout como variables y asignación de su valor correspondiente
        val txtnombre_nuevo: TextView = findViewById(R.id.edtNombre)
        val txtapellido_nuevo: TextView = findViewById(R.id.edtApellidos)
        val txtemail_nuevo: TextView = findViewById(R.id.edtEmailNuevo)
        val txturl_nuevo: TextView = findViewById(R.id.edtImagenPerfil)
        val txtpassword1: TextView = findViewById(R.id.edtPasswordNuevo1)
        val txtpassword2: TextView = findViewById(R.id.edtPasswordNuevo2)
        val btnCrear: Button = findViewById(R.id.btnCrearCuentaNueva)
        val btnCancelar: Button = findViewById(R.id.btn_cancelar)

        // Función asociada al botón "Crear cuenta" que llama a la función createAccount con los parámetros email y password
        btnCrear.setOnClickListener {
            val pass1 = txtpassword1.text.toString()
            val pass2 = txtpassword2.text.toString()

            // Comprobación de que las dos contraseñas introducidas son iguales, en caso afirmativo se llama a la función createAccount
            if (pass1 == pass2) {
                createAccount(txtemail_nuevo.text.toString(), txtpassword1.text.toString())

                val user = hashMapOf(
                    "first_name" to txtnombre_nuevo.text.toString(),
                    "last_name" to txtapellido_nuevo.text.toString(),
                    "email" to txtemail_nuevo.text.toString(),
                    "url" to txturl_nuevo.text.toString()
                )

                db.collection("users").document().set(user)
            } else {
                Toast.makeText(baseContext, "Error en las contraseñas", Toast.LENGTH_LONG).show()
                txtpassword1.requestFocus()
            }
        }


        // Función asociada al botón "Cancelar" que llama a la función onBackPressed()
        btnCancelar.setOnClickListener {
            onBackPressed()
        }

        // Asignación de la instancia de FirebaseAuth
        firebaseAuth = Firebase.auth
    }

    // Función que recibe los parámetros email y password para crear una cuenta nueva en Firebase
    private fun createAccount (email: String, password: String){

        // Comprobación de que los campos email y password no están vacíos, en caso contrario se muestra un mensaje de error
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext, "Rellene los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Creación de la cuenta de usuario en Firebase mediante el uso de la instancia de FirebaseAuth
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){

                // En caso de que la creación de la cuenta sea satisfactoria, se envía un correo de verificación al usuario
                sendEmailVerification()

                // Se muestra un mensaje al usuario indicando que la cuenta se ha creado correctamente
                Toast.makeText(baseContext, "Cuenta creada correctamente, se requiere veificación", Toast.LENGTH_SHORT).show()

                // Se finaliza la actividad actual y se vuelve a la actividad anterior
                onBackPressed()
            }
            else{
                // En caso contrario se muestra un mensaje
                Toast.makeText(baseContext, "Algo salió mal, error" + task.exception, Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun sendEmailVerification(){
        val user = firebaseAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this){ task ->
            if (task.isSuccessful){

            }
            else{

            }
        }
    }
}