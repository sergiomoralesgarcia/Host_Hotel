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
        // Inflar el diseño de la interfaz de usuario del fragmento
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Obtener referencias a las vistas del diseño
        val nameTextView: TextView = view.findViewById(R.id.name_textview)
        val emailTextView: TextView = view.findViewById(R.id.email_textview)
        val profileImageView: ImageView = view.findViewById(R.id.profile_image)

        // Configurar el botón de cerrar sesión
        val signOutButton: Button = view.findViewById(R.id.btn_logout)
        signOutButton.setOnClickListener {
            signOut()
        }

        // Configurar el botón de menú
        val prueba: ImageButton = view.findViewById(R.id.btn_menu)
        prueba.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")
        }

        // Obtener el usuario actualmente autenticado
        val currentUser = firebaseAuth.currentUser
        if (isAdded && currentUser != null) {
            // Si el fragmento está agregado y el usuario está autenticado, mostrar información de perfil
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
                // Cargar la imagen de perfil utilizando Glide si hay una URL de foto disponible
                Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(profileImageView)
            } else {
                // Establecer una imagen de perfil predeterminada si no hay URL de foto disponible
                profileImageView.setImageResource(R.drawable.user)
            }
        } else {
            // Si no hay usuario autenticado, mostrar valores predeterminados
            nameTextView.text = "Nombre"
            emailTextView.text = "Correo electrónico"
        }

        return view
    }

    private fun signOut() {
        // Mostrar un cuadro de diálogo de confirmación para cerrar sesión
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.ttl_close_prof)
        builder.setMessage(R.string.ttl_close_ask)
        builder.setPositiveButton(R.string.ttl_yes) { dialog, which ->
            // Cerrar sesión y redirigir al inicio de sesión
            firebaseAuth.signOut()
            Toast.makeText(requireContext(), R.string.ttl_close_success, Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        builder.setNegativeButton(R.string.ttl_no) { dialog, which ->
            // No hacer nada si se cancela el cierre de sesión
        }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
