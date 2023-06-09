import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.hosthotel.Hotel
import com.tfg.hosthotel.R

class AddFragment : Fragment() {

    private lateinit var edtUrl: TextInputEditText
    private lateinit var edtNombre: TextInputEditText
    private lateinit var edtCiudad: TextInputEditText
    private lateinit var edtStreet: TextInputEditText
    private lateinit var edtInfo: TextInputEditText
    private lateinit var btnAddHotel: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        edtUrl = view.findViewById(R.id.edtUrl)
        edtNombre = view.findViewById(R.id.edtNombre)
        edtCiudad = view.findViewById(R.id.edtCiudad)
        edtStreet = view.findViewById(R.id.edtStreet)
        edtInfo = view.findViewById(R.id.edtInfo)
        btnAddHotel = view.findViewById(R.id.btnAddHotel)

        // Inicializa la instancia de FirebaseFirestore
        db = FirebaseFirestore.getInstance()

        btnAddHotel.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val ciudad = edtCiudad.text.toString()
            val street = edtStreet.text.toString()
            val url = edtUrl.text.toString()
            val info = edtInfo.text.toString()

            if (nombre.isNotEmpty() && ciudad.isNotEmpty() && street.isNotEmpty() && url.isNotEmpty() && info.isNotEmpty()) {
                val hotel = Hotel(nombre, ciudad, street, url, info)

                // Guarda el objeto "hotel" en tu base de datos
                db.collection("hotels")
                    .add(hotel)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(requireContext(), "Hotel añadido correctamente", Toast.LENGTH_SHORT).show()
                        // Puedes realizar alguna acción adicional después de guardar el hotel, si es necesario.

                        // Aquí puedes agregar el código para limpiar los campos de entrada de texto si lo deseas.
                        edtNombre.text = null
                        edtCiudad.text = null
                        edtStreet.text = null
                        edtUrl.text = null
                        edtInfo.text = null

                        // Redirige automáticamente al fragmento HomeFragment después de añadir un hotel correctamente
                        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                        fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, HomeFragment())
                            .commit()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error al añadir el hotel", Toast.LENGTH_SHORT).show()
                        // Puedes realizar alguna acción adicional en caso de error.
                    }
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
