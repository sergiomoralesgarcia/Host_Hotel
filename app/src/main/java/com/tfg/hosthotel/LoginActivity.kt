package com.tfg.hosthotel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private val RC_SIGN_IN = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencia a los elementos de la vista
        val btningresar: Button = findViewById(R.id.btnIngresar)
        val btnGoogleSignIn: Button = findViewById(R.id.btngoogle)
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

        // Configuración de inicio de sesión con Google
        btnGoogleSignIn.setOnClickListener {
            signInWithGoogle()
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

        // Verificar si hay un usuario actualmente autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Si hay un usuario autenticado, ir directamente a la página principal
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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

    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        // Desconectar cualquier sesión activa de Google antes de iniciar una nueva sesión
        GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()

        // Iniciar la actividad de inicio de sesión con Google
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Resultado de la actividad de inicio de sesión con Google
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Autenticación con Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Si falla la autenticación con Google, se muestra un mensaje de error
                Toast.makeText(this, "Error de inicio de sesión con Google: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Si la autenticación con Firebase es exitosa, el usuario se autentica
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()

                    // Accedemos a la página principal
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                } else {
                    // Si la autenticación falla, mostrar un mensaje de error
                    Toast.makeText(baseContext, "Error al autenticarse con Firebase", Toast.LENGTH_SHORT).show()
                }
            }
    }



    // Deshabilita el botón "back"
    override fun onBackPressed() {
        return
    }
}
