import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tfg.hosthotel.Hotel
import com.tfg.hosthotel.R

class MyAdapter(private val hotelList: ArrayList<Hotel>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

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
    }

    override fun getItemCount(): Int {
        return hotelList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txt_name)
        val city: TextView = itemView.findViewById(R.id.txt_ciudad)
        val imageView: ImageView = itemView.findViewById(R.id.hotelImage)
    }
}
