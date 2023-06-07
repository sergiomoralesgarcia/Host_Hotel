package com.tfg.hosthotel.fragments

import MyAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.hosthotel.Hotel
import com.tfg.hosthotel.R

class HomeFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var hotelRecyclerView: RecyclerView
    private lateinit var hotelArrayList: ArrayList<Hotel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        hotelRecyclerView = view.findViewById(R.id.hotelList)
        hotelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        hotelRecyclerView.setHasFixedSize(true)

        hotelArrayList = arrayListOf<Hotel>()
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

            hotelRecyclerView.adapter = MyAdapter(hotelArrayList)
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }
}
