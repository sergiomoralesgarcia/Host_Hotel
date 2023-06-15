import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.hosthotel.DetailActivity
import com.tfg.hosthotel.EditActivity
import com.tfg.hosthotel.objects.Hotel
import com.tfg.hosthotel.R
import com.tfg.hosthotel.adapters.MyAdapter

class HomeFragment : Fragment(), MyAdapter.OnItemClickListener, MyAdapter.OnItemEditClickListener, MyAdapter.OnItemLongClickListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var hotelRecyclerView: RecyclerView
    private lateinit var hotelArrayList: ArrayList<Hotel>
    private lateinit var filteredHotelArrayList: ArrayList<Hotel>

    private lateinit var llContenedor: LinearLayout
    private lateinit var llCargando: LinearLayout

    private var selectedCity: String? = null
    private lateinit var buttons: List<Button>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicializar RecyclerView y configurar su diseño
        hotelRecyclerView = view.findViewById(R.id.hotelList)
        hotelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        hotelRecyclerView.setHasFixedSize(true)

        // Inicializar listas de hoteles
        hotelArrayList = arrayListOf()
        filteredHotelArrayList = arrayListOf()

        // Obtener referencias a los botones de la ciudad
        val btnCadiz = view.findViewById<Button>(R.id.btnCadiz)
        val btnCordoba = view.findViewById<Button>(R.id.btnCordoba)
        val btnGranada = view.findViewById<Button>(R.id.btnGranada)
        val btnMalaga = view.findViewById<Button>(R.id.btnMalaga)
        val btnSevilla = view.findViewById<Button>(R.id.btnSevilla)
        val btnJaen = view.findViewById<Button>(R.id.btnJaen)
        val btnAlmeria = view.findViewById<Button>(R.id.btnAlmeria)
        val btnHuelva = view.findViewById<Button>(R.id.btnHuelva)

        // Crear lista de botones
        buttons = listOf(btnCadiz, btnCordoba, btnGranada, btnMalaga, btnSevilla, btnJaen, btnAlmeria, btnHuelva)

        // Configurar onClickListeners para los botones de la ciudad
        btnCadiz.setOnClickListener { toggleCitySelection("Cádiz", btnCadiz) }
        btnCordoba.setOnClickListener { toggleCitySelection("Córdoba", btnCordoba) }
        btnGranada.setOnClickListener { toggleCitySelection("Granada", btnGranada) }
        btnMalaga.setOnClickListener { toggleCitySelection("Málaga", btnMalaga) }
        btnSevilla.setOnClickListener { toggleCitySelection("Sevilla", btnSevilla) }
        btnJaen.setOnClickListener { toggleCitySelection("Jaén", btnJaen) }
        btnAlmeria.setOnClickListener { toggleCitySelection("Almería", btnAlmeria) }
        btnHuelva.setOnClickListener { toggleCitySelection("Huelva", btnHuelva) }

        // Obtener referencias a los elementos de animación de carga
        llContenedor = view.findViewById(R.id.llContenedor)
        llCargando = view.findViewById(R.id.llCargando)

        getHotelData()

        return view
    }

    private fun getHotelData() {
        // Inicializar instancia de Firebase Firestore
        db = FirebaseFirestore.getInstance()
        val hotelsCollection = db.collection("hotels")

        // Obtener documentos de la colección "hotels"
        hotelsCollection.get().addOnSuccessListener { snapshot ->
            for (document in snapshot.documents) {
                val hotel = document.toObject(Hotel::class.java)
                hotelArrayList.add(hotel!!)
            }

            // Copiar todos los hoteles al ArrayList filtrado
            filteredHotelArrayList.addAll(hotelArrayList)

            // Crear y configurar adaptador para el RecyclerView
            val adapter = MyAdapter(filteredHotelArrayList, this, this)
            adapter.setOnItemLongClickListener(this)
            hotelRecyclerView.adapter = adapter

            showData()
        }.addOnFailureListener { exception ->
        }
    }

    private fun showData() {
        // Ocultar elementos de carga y mostrar contenido
        llCargando.isVisible = false
        llContenedor.isVisible = true
    }

    private fun toggleCitySelection(city: String, button: Button) {
        if (selectedCity == city) {
            // Si ya se seleccionó la misma ciudad, deseleccionarla
            selectedCity = null
            updateButtonStates()
            updateHotelList()
        } else {
            // Si se selecciona una ciudad diferente, actualizar la selección y filtrar los hoteles
            selectedCity = city
            updateButtonStates()
            filterHotelsByCity(city)
        }
    }

    private fun updateButtonStates() {
        // Actualizar el estado de los botones de la ciudad según la selección
        for (button in buttons) {
            if (button.text == selectedCity) {
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.boton))
            } else {
                button.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_selector)
            }
        }
    }

    private fun filterHotelsByCity(city: String) {
        // Filtrar los hoteles por la ciudad seleccionada
        filteredHotelArrayList.clear()
        filteredHotelArrayList.addAll(hotelArrayList.filter { hotel -> hotel.localtion_hotel == city })
        hotelRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun updateHotelList() {
        // Actualizar la lista de hoteles sin filtrar
        filteredHotelArrayList.clear()
        filteredHotelArrayList.addAll(hotelArrayList)
        hotelRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onItemClick(hotel: Hotel) {
        // Acción al hacer clic en un hotel (abrir detalle del hotel)
        openHotelDetail(hotel)
    }

    override fun onItemEditClick(hotel: Hotel) {
        // Acción al hacer clic en el botón de edición de un hotel (abrir actividad de edición)
        openEditActivity(hotel)
    }

    override fun onItemLongClick(hotel: Hotel) {
        // Acción al hacer clic largo en un hotel (eliminar el hotel)
        deleteHotelFromFirestore(hotel)
    }

    private fun openHotelDetail(hotel: Hotel) {
        // Abrir actividad de detalle del hotel
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("hotelImage", hotel.url_img)
        intent.putExtra("hotelName", hotel.name_hotel)
        intent.putExtra("hotelCity", hotel.localtion_hotel)
        intent.putExtra("hotelStreet", hotel.street_hotel)
        intent.putExtra("hotelInfo", hotel.info_hotel)
        startActivity(intent)
    }

    private fun openEditActivity(hotel: Hotel) {
        // Abrir actividad de edición del hotel
        val intent = Intent(requireContext(), EditActivity::class.java)
        intent.putExtra("hotelName", hotel.name_hotel)
        startActivity(intent)
    }

    private fun deleteHotelFromFirestore(hotel: Hotel) {
        // Mostrar diálogo de confirmación para eliminar el hotel
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(getString(R.string.ttl_confirm_delete))
        alertDialogBuilder.setMessage(R.string.ttl_ask_delete)
        alertDialogBuilder.setPositiveButton(R.string.ttl_delete) { dialog, _ ->
            // Eliminar el hotel de Firebase Firestore
            val db = FirebaseFirestore.getInstance()
            val hotelsCollection = db.collection("hotels")

            hotelsCollection
                .whereEqualTo("name_hotel", hotel.name_hotel)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty) {
                        val document = snapshot.documents[0]
                        document.reference.delete()
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), R.string.ttl_delete_success, Toast.LENGTH_SHORT).show()
                                // Actualizar el RecyclerView después de eliminar el hotel
                                hotelArrayList.remove(hotel)
                                filteredHotelArrayList.remove(hotel)
                                hotelRecyclerView.adapter?.notifyDataSetChanged()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(requireContext(), R.string.ttl_delete_error, Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(requireContext(), R.string.ttl_dont_fund, Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), R.string.ttl_delete_error, Toast.LENGTH_SHORT).show()
                }
        }
        alertDialogBuilder.setNegativeButton(R.string.btn_cancel) { dialog, _ ->
            // Cancelar eliminación
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
