package com.tfg.hosthotel.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.hosthotel.R
import com.tfg.hosthotel.login.LoginActivity
import com.tfg.hosthotel.menus.BottomSheetFragment

class ProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // ...
        }
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val nameTextView: TextView = view.findViewById(R.id.name_textview)
        val emailTextView: TextView = view.findViewById(R.id.email_textview)

        val signOutButton: Button = view.findViewById(R.id.btn_logout)
        signOutButton.setOnClickListener {
            signOut()
        }

        val prueba: ImageButton = view.findViewById(R.id.btn_menu)
        prueba.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")
        }

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            emailTextView.text = email

            val userId = currentUser.uid
            val userRef = firestore.collection("users").document(userId)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("email")
                        if (firstName != null && firstName.isNotEmpty()) {
                            nameTextView.text = firstName
                        } else {
                            nameTextView.text = "Nombre"
                        }
                    } else {
                        nameTextView.text = "Nombre"
                    }
                }
                .addOnFailureListener { exception ->
                    nameTextView.text = "Nombre"
                    Toast.makeText(activity?.baseContext, "Error al obtener el nombre del usuario", Toast.LENGTH_SHORT).show()
                }

        } else {
            nameTextView.text = "Nombre"
            emailTextView.text = "Correo electrónico"
        }

        return view
    }

    private fun signOut() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cerrar sesión")
        builder.setMessage("¿Estás seguro de que quieres cerrar sesión?")
        builder.setPositiveButton("Sí") { dialog, which ->
            firebaseAuth.signOut()
            Toast.makeText(activity?.baseContext, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            // No hacer nada si se selecciona "No"
        }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
