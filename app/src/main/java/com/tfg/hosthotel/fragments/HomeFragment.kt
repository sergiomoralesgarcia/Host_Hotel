import MyAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.hosthotel.DetailActivity
import com.tfg.hosthotel.Hotel
import com.tfg.hosthotel.R

class HomeFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var hotelRecyclerView: RecyclerView
    private lateinit var hotelArrayList: ArrayList<Hotel>

    lateinit var llContenedor: LinearLayout
    lateinit var llCargando: LinearLayout

    private var selectedCity: String? = null
    private lateinit var buttons: List<Button>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        hotelRecyclerView = view.findViewById(R.id.hotelList)
        hotelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        hotelRecyclerView.setHasFixedSize(true)

        hotelArrayList = arrayListOf<Hotel>()

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

            val adapter = MyAdapter(hotelArrayList, object : MyAdapter.OnItemClickListener {
                override fun onItemClick(hotel: Hotel) {
                    openHotelDetail(hotel)
                }
            })
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
        val filteredList = hotelArrayList.filter { hotel -> hotel.localtion_hotel == city }
        val adapter = MyAdapter(filteredList as ArrayList<Hotel>, object : MyAdapter.OnItemClickListener {
            override fun onItemClick(hotel: Hotel) {
                openHotelDetail(hotel)
            }
        })
        hotelRecyclerView.adapter = adapter
    }

    private fun updateHotelList() {
        val adapter = MyAdapter(hotelArrayList, object : MyAdapter.OnItemClickListener {
            override fun onItemClick(hotel: Hotel) {
                openHotelDetail(hotel)
            }
        })
        hotelRecyclerView.adapter = adapter
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

}
