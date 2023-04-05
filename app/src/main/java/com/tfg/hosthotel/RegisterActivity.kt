package com.tfg.hosthotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtnombre_nuevo: TextView = findViewById(R.id.edtNombre)
        val txtemail_nuevo: TextView = findViewById(R.id.edtEmailNuevo)
        val txtpassword1: TextView = findViewById(R.id.edtPasswordNuevo1)
        val txtpassword2: TextView = findViewById(R.id.edtPasswordNuevo2)
        val btnCrear: Button = findViewById(R.id.btnCrearCuentaNueva)
        val btnCancelar: Button = findViewById(R.id.btn_cancelar)

        btnCrear.setOnClickListener{
            var pass1 = txtpassword1.text.toString()
            var pass2 = txtpassword2.text.toString()

            if (pass1.equals(pass2)){
                createAccount(txtemail_nuevo.text.toString(), txtpassword1.text.toString())
            }
            else{
                Toast.makeText(baseContext, "Error en las contraseñas", Toast.LENGTH_LONG).show()
                txtpassword1.requestFocus()
            }
        }

        btnCancelar.setOnClickListener {
            onBackPressed()
        }

        firebaseAuth = Firebase.auth
    }

    private fun createAccount (email: String, password: String){

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext, "Rellene los campos", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                sendEmailVerification()
                Toast.makeText(baseContext, "Cuenta creada correctamente, se requiere veificación", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            else{
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