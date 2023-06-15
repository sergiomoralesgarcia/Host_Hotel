package com.tfg.hosthotel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tfg.hosthotel.R
import com.tfg.hosthotel.objects.Review

class ReviewAdapter(private val reviewList: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewName: TextView = itemView.findViewById(R.id.tvReviewName)
        val reviewText: TextView = itemView.findViewById(R.id.tvReviewText)
        val reviewDate: TextView = itemView.findViewById(R.id.tvReviewDate)

        fun bind(review: Review) {
            reviewName.text = review.userName
            reviewText.text = review.reviewText
            reviewDate.text = review.currentDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentReview = reviewList[position]
        holder.bind(currentReview)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}
