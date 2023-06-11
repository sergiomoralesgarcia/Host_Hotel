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
                val displayName = "${txtnombre_nuevo.text} ${txtapellido_nuevo.text}"
                createAccount(txtemail_nuevo.text.toString(), txtpassword1.text.toString(), displayName, txturl_nuevo.text.toString())

                val user = hashMapOf(
                    "first_name" to txtnombre_nuevo.text.toString(),
                    "last_name" to txtapellido_nuevo.text.toString(),
                    "email" to txtemail_nuevo.text.toString(),
                    "url" to txturl_nuevo.text.toString()
                )

                db.collection("users").document(user["email"] as String).set(user)
                    .addOnSuccessListener {
                        Toast.makeText(baseContext, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(baseContext, "Error al registrar el usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(baseContext, "Error en las contraseñas", Toast.LENGTH_LONG).show()
                txtpassword1.requestFocus()
            }
        }

        btnCancelar.setOnClickListener {
            onBackPressed()
        }

        firebaseAuth = Firebase.auth
    }

    private fun createAccount(email: String, password: String, displayName: String, url: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext, "Rellene los campos", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(url)) // Agregar la URL de la imagen de perfil
                    .build()

                user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                    if (profileTask.isSuccessful) {
                        val userUpdate = hashMapOf<String, Any>(
                            "url" to url
                        )

                        db.collection("users").document(email).update(userUpdate)
                            .addOnSuccessListener {
                                Toast.makeText(baseContext, "URL de la imagen de perfil guardada correctamente", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(baseContext, "Error al guardar la URL de la imagen de perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        sendEmailVerification()

                        Toast.makeText(baseContext, "Cuenta creada correctamente, se requiere verificación", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Toast.makeText(baseContext, "Error al establecer el nombre de usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(baseContext, "Algo salió mal, error: ${task.exception}", Toast.LENGTH_SHORT).show()
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
