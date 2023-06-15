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
import com.tfg.hosthotel.Hotel
import com.tfg.hosthotel.R

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

        hotelRecyclerView = view.findViewById(R.id.hotelList)
        hotelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        hotelRecyclerView.setHasFixedSize(true)

        hotelArrayList = arrayListOf()
        filteredHotelArrayList = arrayListOf()

        val btnCadiz = view.findViewById<Button>(R.id.btnCadiz)
        val btnCordoba = view.findViewById<Button>(R.id.btnCordoba)
        val btnGranada = view.findViewById<Button>(R.id.btnGranada)
        val btnMalaga = view.findViewById<Button>(R.id.btnMalaga)
        val btnSevilla = view.findViewById<Button>(R.id.btnSevilla)
        val btnJaen = view.findViewById<Button>(R.id.btnJaen)
        val btnAlmeria = view.findViewById<Button>(R.id.btnAlmeria)
        val btnHuelva = view.findViewById<Button>(R.id.btnHuelva)

        buttons = listOf(btnCadiz, btnCordoba, btnGranada, btnMalaga, btnSevilla, btnJaen, btnAlmeria, btnHuelva)

        btnCadiz.setOnClickListener { toggleCitySelection("Cádiz", btnCadiz) }
        btnCordoba.setOnClickListener { toggleCitySelection("Córdoba", btnCordoba) }
        btnGranada.setOnClickListener { toggleCitySelection("Granada", btnGranada) }
        btnMalaga.setOnClickListener { toggleCitySelection("Málaga", btnMalaga) }
        btnSevilla.setOnClickListener { toggleCitySelection("Sevilla", btnSevilla) }
        btnJaen.setOnClickListener { toggleCitySelection("Jaén", btnJaen) }
        btnAlmeria.setOnClickListener { toggleCitySelection("Almería", btnAlmeria) }
        btnHuelva.setOnClickListener { toggleCitySelection("Huelva", btnHuelva) }

        // Agrega más botones y acciones para las demás provincias de Andalucía

        // Valores de la animación de carga
        llContenedor = view.findViewById(R.id.llContenedor)
        llCargando = view.findViewById(R.id.llCargando)

        getHotelData()

        return view
    }

    private fun getHotelData() {
        db = FirebaseFirestore.getInstance()
        val hotelsCollection = db.collection("hotels")

        hotelsCollection.get().addOnSuccessListener { snapshot ->
            for (document in snapshot.documents) {
                val hotel = document.toObject(Hotel::class.java)
                hotelArrayList.add(hotel!!)
            }

            filteredHotelArrayList.addAll(hotelArrayList)

            val adapter = MyAdapter(filteredHotelArrayList, this, this)

            adapter.setOnItemLongClickListener(this)

            hotelRecyclerView.adapter = adapter

            // Una vez que hayas procesado los datos, llama a la función showData()
            showData()
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    private fun showData() {
        llCargando.isVisible = false
        llContenedor.isVisible = true
    }

    private fun toggleCitySelection(city: String, button: Button) {
        if (selectedCity == city) {
            selectedCity = null
            updateButtonStates()
            updateHotelList()
        } else {
            selectedCity = city
            updateButtonStates()
            filterHotelsByCity(city)
        }
    }

    private fun updateButtonStates() {
        for (button in buttons) {
            if (button.text == selectedCity) {
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.boton))
            } else {
                button.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_selector)
            }
        }
    }

    private fun filterHotelsByCity(city: String) {
        filteredHotelArrayList.clear()
        filteredHotelArrayList.addAll(hotelArrayList.filter { hotel -> hotel.localtion_hotel == city })
        hotelRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun updateHotelList() {
        filteredHotelArrayList.clear()
        filteredHotelArrayList.addAll(hotelArrayList)
        hotelRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onItemClick(hotel: Hotel) {
        openHotelDetail(hotel)
    }

    override fun onItemEditClick(hotel: Hotel) {
        openEditActivity(hotel)
    }

    override fun onItemLongClick(hotel: Hotel) {
        deleteHotelFromFirestore(hotel)
    }

    private fun openHotelDetail(hotel: Hotel) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("hotelImage", hotel.url_img)
        intent.putExtra("hotelName", hotel.name_hotel)
        intent.putExtra("hotelCity", hotel.localtion_hotel)
        intent.putExtra("hotelStreet", hotel.street_hotel)
        intent.putExtra("hotelInfo", hotel.info_hotel)
        startActivity(intent)
    }

    private fun openEditActivity(hotel: Hotel) {
        val intent = Intent(requireContext(), EditActivity::class.java)
        intent.putExtra("hotelName", hotel.name_hotel)
        startActivity(intent)
    }

    private fun deleteHotelFromFirestore(hotel: Hotel) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(getString(R.string.ttl_confirm_delete))
        alertDialogBuilder.setMessage(R.string.ttl_ask_delete)
        alertDialogBuilder.setPositiveButton(R.string.ttl_delete) { dialog, _ ->
            // Eliminar el hotel
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
                                // Actualizar el RecyclerView
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
