import MyAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.hosthotel.Hotel
import com.tfg.hosthotel.R

class HomeFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var hotelRecyclerView: RecyclerView
    private lateinit var hotelArrayList: ArrayList<Hotel>

    private var selectedCity: String? = null
    private lateinit var buttons: List<Button>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        hotelRecyclerView = view.findViewById(R.id.hotelList)
        hotelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        hotelRecyclerView.setHasFixedSize(true)

        hotelArrayList = arrayListOf<Hotel>()
        getHotelData()

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

            hotelRecyclerView.adapter = MyAdapter(hotelArrayList)
        }.addOnFailureListener { exception ->
            // Handle failure
        }
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
        val filteredList = hotelArrayList.filter { hotel -> hotel.localtion_hotel == city }
        val adapter = MyAdapter(filteredList as ArrayList<Hotel>)
        hotelRecyclerView.adapter = adapter
    }

    private fun updateHotelList() {
        val adapter = MyAdapter(hotelArrayList)
        hotelRecyclerView.adapter = adapter
    }
}
