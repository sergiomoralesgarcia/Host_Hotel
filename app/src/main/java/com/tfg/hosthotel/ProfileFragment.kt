package com.tfg.hosthotel

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// Declaración de la instancia de FirebaseAuth
private lateinit var firebaseAuth: FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    // Declaración de los parámetros que recibe el fragmento
    private var param1: String? = null
    private var param2: String? = null

    // Sobrescritura del método onCreate del ciclo de vida del fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Se comprueban los argumentos que recibe el fragmento
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        // Se obtiene la instancia de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    // Sobrescritura del método onCreateView del ciclo de vida del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño de la vista del fragmento
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Obtener la referencia del botón de cerrar sesión
        val signOutButton: Button = view.findViewById(R.id.btn_logout)
        // Establecer el evento onclick del botón de cerrar sesión
        signOutButton.setOnClickListener {
            signOut()
        }

        // Obtener la referencia del botón de mostrar el BottomSheet
        val showBottomSheetButton: ImageButton = view.findViewById(R.id.btn_show_bottom_sheet)
        // Establecer el evento onclick del botón de mostrar el BottomSheet
        showBottomSheetButton.setOnClickListener {
            showBottomSheet()
        }

        // Devolver la vista inflada del fragmento
        return view
    }

    // Método que realiza la acción de cerrar sesión del usuario
    private fun signOut() {
        // Crear un diálogo de alerta para confirmar si se quiere cerrar sesión
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cerrar sesión")
        builder.setMessage("¿Estás seguro de que quieres cerrar sesión?")
        builder.setPositiveButton("Sí") { dialog, which ->
            // Si se confirma la acción, se cierra la sesión, se muestra un mensaje y se redirige al login
            firebaseAuth.signOut()
            Toast.makeText(activity?.baseContext, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            // No hacer nada si se selecciona "No"
        }
        // Crear el diálogo y mostrarlo
        val dialog = builder.create()
        dialog.show()
        }

    private fun showBottomSheet() {
        val bottomSheet = BottomSheetFragment()
        bottomSheet.show(requireFragmentManager(), "BottomSheetDialog")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class BottomSheetFragment : BottomSheetDialogFragment() {

    // Función que infla el layout del BottomSheetDialogFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)
    }

}
