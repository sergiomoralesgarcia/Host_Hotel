import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.hosthotel.Hotel
import com.tfg.hosthotel.R


class AddFragment : Fragment() {

    private lateinit var edtUrl: TextInputEditText
    private lateinit var edtNombre: TextInputEditText
    private lateinit var spinnerCiudad: Spinner
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
        spinnerCiudad = view.findViewById(R.id.edtCiudad)
        edtStreet = view.findViewById(R.id.edtStreet)
        edtInfo = view.findViewById(R.id.edtInfo)
        btnAddHotel = view.findViewById(R.id.btnAddHotel)

        // Inicializa la instancia de FirebaseFirestore
        db = FirebaseFirestore.getInstance()

        // Obtener el array de ciudades desde los recursos
        val cities = resources.getStringArray(R.array.cities_array)

        // Crear el ArrayAdapter utilizando el array de ciudades y un layout predefinido para los items del Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)

        // Especificar el layout que se utilizará cuando se despliegue la lista de opciones
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Establecer el ArrayAdapter en el Spinner
        spinnerCiudad.adapter = adapter

        btnAddHotel.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val ciudad = spinnerCiudad.selectedItem.toString()
            val street = edtStreet.text.toString()
            val url = edtUrl.text.toString()
            val info = edtInfo.text.toString()

            if (nombre.isNotEmpty() && ciudad.isNotEmpty() && street.isNotEmpty() && url.isNotEmpty() && info.isNotEmpty()) {
                val hotel = Hotel(nombre, ciudad, street, url, info)

                // Guarda el objeto "hotel" en tu base de datos
                db.collection("hotels")
                    .add(hotel)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(requireContext(), getString(R.string.add_success), Toast.LENGTH_SHORT).show()
                        // Puedes realizar alguna acción adicional después de guardar el hotel, si es necesario.

                        // Aquí puedes agregar el código para limpiar los campos de entrada de texto si lo deseas.
                        edtNombre.text = null
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
                        Toast.makeText(requireContext(),  getString(R.string.add_error), Toast.LENGTH_SHORT).show()
                        // Puedes realizar alguna acción adicional en caso de error.
                    }
            } else {
                Toast.makeText(requireContext(), getString(R.string.write_full), Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
