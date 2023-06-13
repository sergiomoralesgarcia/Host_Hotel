import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.tfg.hosthotel.Hotel
import com.tfg.hosthotel.R

class MyAdapter(
    private val hotelList: ArrayList<Hotel>,
    private val itemClickListener: OnItemClickListener,
    private val itemEditClickListener: OnItemEditClickListener
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var longClickListener: OnItemLongClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(hotel: Hotel)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(hotel: Hotel)
    }

    interface OnItemEditClickListener {
        fun onItemEditClick(hotel: Hotel)
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        longClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_hotel,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = hotelList[position]

        holder.name.text = currentItem.name_hotel
        holder.city.text = currentItem.localtion_hotel
        Picasso.get().load(currentItem.url_img).into(holder.imageView)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentItem)
        }

        holder.itemView.setOnLongClickListener {
            longClickListener?.onItemLongClick(currentItem)
            true
        }

        holder.itemView.findViewById<View>(R.id.btn_edit)?.setOnClickListener {
            itemEditClickListener.onItemEditClick(currentItem)
        }

        val favButton = holder.itemView.findViewById<ImageView>(R.id.btn_fav)
        if (currentItem.isFavorite) {
            favButton.setImageResource(R.drawable.ic_fav_on)
        } else {
            favButton.setImageResource(R.drawable.ic_fav_off)
        }

        // Dentro del bloque `onClickListener` de `favButton`
        favButton.setOnClickListener {
            currentItem.isFavorite = !currentItem.isFavorite
            updateFavoriteStatus(currentItem, currentItem.isFavorite) // Pasar el estado de favorito como segundo argumento
            if (currentItem.isFavorite) {
                favButton.setImageResource(R.drawable.ic_fav_on)
            } else {
                favButton.setImageResource(R.drawable.ic_fav_off)
            }
        }

    }

    override fun getItemCount(): Int {
        return hotelList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txt_name)
        val city: TextView = itemView.findViewById(R.id.txt_ciudad)
        val imageView: ImageView = itemView.findViewById(R.id.hotelImage)
    }

    private fun updateFavoriteStatus(hotel: Hotel, isFavorite: Boolean) {
        val db = FirebaseFirestore.getInstance()
        val hotelsCollection = db.collection("hotels")
        val query = hotelsCollection.whereEqualTo("name_hotel", hotel.name_hotel)

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val hotelRef = hotelsCollection.document(document.id)
                    hotelRef.update("isFavorite", isFavorite)
                        .addOnSuccessListener {
                            // El estado de favorito se actualizó correctamente en Firestore
                        }
                        .addOnFailureListener { exception ->
                            // Error al actualizar el estado de favorito en Firestore
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Error al obtener los documentos de Firestore
            }
    }
}
