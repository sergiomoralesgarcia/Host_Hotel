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
import com.tfg.hosthotel.objects.Hotel
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

        val cities = resources.getStringArray(R.array.cities_array)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCiudad.adapter = adapter

        btnAddHotel.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val ciudad = spinnerCiudad.selectedItem.toString()
            val street = edtStreet.text.toString()
            val url = edtUrl.text.toString()
            val info = edtInfo.text.toString()

            if (nombre.isNotEmpty() && ciudad.isNotEmpty() && street.isNotEmpty() && url.isNotEmpty() && info.isNotEmpty()) {
                val hotel = Hotel(nombre, ciudad, street, url, info)

                // Guarda el objeto hotel en la base de datos
                db.collection("hotels")
                    .add(hotel)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(requireContext(), getString(R.string.add_success), Toast.LENGTH_SHORT).show()

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
                    }
            } else {
                Toast.makeText(requireContext(), getString(R.string.write_full), Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
