package com.tfg.hosthotel.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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
        val profileImageView: ImageView = view.findViewById(R.id.profile_image)

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
        if (isAdded && currentUser != null) {
            val email = currentUser.email
            emailTextView.text = email

            val displayName = currentUser.displayName
            if (displayName != null && displayName.isNotEmpty()) {
                nameTextView.text = displayName
            } else {
                nameTextView.text = "Nombre"
            }

            val photoUrl = currentUser.photoUrl.toString()
            if (!photoUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(profileImageView)
            } else {
                profileImageView.setImageResource(R.drawable.user)
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
            Toast.makeText(requireContext(), "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
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
