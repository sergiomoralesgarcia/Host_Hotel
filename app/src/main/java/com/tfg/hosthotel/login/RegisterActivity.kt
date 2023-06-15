package com.tfg.hosthotel.login

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tfg.hosthotel.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Obtener referencias a las vistas del diseño
        val txtnombre_nuevo: TextView = findViewById(R.id.edtNombre)
        val txtapellido_nuevo: TextView = findViewById(R.id.edtApellidos)
        val txtemail_nuevo: TextView = findViewById(R.id.edtEmailNuevo)
        val txturl_nuevo: TextView = findViewById(R.id.edtImagenPerfil)
        val txtpassword1: TextView = findViewById(R.id.edtPasswordNuevo1)
        val txtpassword2: TextView = findViewById(R.id.edtPasswordNuevo2)
        val btnCrear: Button = findViewById(R.id.btnCrearCuentaNueva)
        val btnCancelar: Button = findViewById(R.id.btn_cancelar)

        btnCrear.setOnClickListener {
            val pass1 = txtpassword1.text.toString()
            val pass2 = txtpassword2.text.toString()

            if (pass1 == pass2) {
                // Crear cuenta de usuario con la información proporcionada
                val displayName = "${txtnombre_nuevo.text} ${txtapellido_nuevo.text}"
                createAccount(txtemail_nuevo.text.toString(), txtpassword1.text.toString(), displayName, txturl_nuevo.text.toString())

                // Guardar información adicional del usuario en Firestore
                val user = hashMapOf(
                    "first_name" to txtnombre_nuevo.text.toString(),
                    "last_name" to txtapellido_nuevo.text.toString(),
                    "email" to txtemail_nuevo.text.toString(),
                    "url" to txturl_nuevo.text.toString()
                )

                db.collection("users").document(user["email"] as String).set(user)
                    .addOnSuccessListener {
                        Toast.makeText(baseContext,R.string.txt_user_success, Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(baseContext, "Error al registrar el usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Mostrar un mensaje de error si las contraseñas no coinciden
                Toast.makeText(baseContext, R.string.txt_pass_error, Toast.LENGTH_LONG).show()
                txtpassword1.requestFocus()
            }
        }

        btnCancelar.setOnClickListener {
            // Volver atrás cuando se cancela el registro
            onBackPressed()
        }

        // Obtener la instancia de FirebaseAuth
        firebaseAuth = Firebase.auth
    }

    private fun createAccount(email: String, password: String, displayName: String, url: String) {
        // Verificar que el correo electrónico y la contraseña no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext,R.string.txt_rellenar, Toast.LENGTH_SHORT).show()
            return
        }

        // Crear la cuenta de usuario en Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser

                // Actualizar el perfil del usuario con el nombre de pantalla y la URL de la imagen de perfil
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(url)) // Agregar la URL de la imagen de perfil
                    .build()

                user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                    if (profileTask.isSuccessful) {
                        val userUpdate = hashMapOf<String, Any>(
                            "url" to url
                        )

                        // Actualizar la URL de la imagen de perfil en Firestore
                        db.collection("users").document(email).update(userUpdate)
                            .addOnSuccessListener {
                                Toast.makeText(baseContext,R.string.txt_url_success, Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(baseContext, "Error al guardar la URL de la imagen de perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        // Enviar correo de verificación al usuario
                        sendEmailVerification()

                        Toast.makeText(baseContext,R.string.txt_account_success, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Toast.makeText(baseContext, R.string.txt_user_error, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(baseContext,"Algo salió mal, error: ${task.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendEmailVerification() {
        val user = firebaseAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // El correo de verificación se envió correctamente
            } else {
                // Error al enviar el correo de verificación
            }
        }
    }
}

