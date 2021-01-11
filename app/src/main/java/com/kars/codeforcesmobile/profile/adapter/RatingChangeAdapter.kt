package com.kars.codeforcesmobile.profile.adapter

import android.text.SpannableStringBuilder
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.kars.codeforcesmobile.R
import com.kars.codeforcesmobile.convertEpochToStringDate
import com.kars.codeforcesmobile.profile.data.RatingChange
import kotlinx.android.synthetic.main.rating_changes_item.view.*

class RatingChangeAdapter : RecyclerView.Adapter<RatingChangeAdapter.MyViewHolder>() {
    private var ratingChangeList: List<RatingChange.Rating> = ArrayList<RatingChange.Rating>()

    //todo change return of epochToString
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = View.inflate(parent.context, R.layout.rating_changes_item, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(ratingChangeList[position])
    }

    override fun getItemCount() = ratingChangeList.size

    fun fillData(ratingChangeList: List<RatingChange.Rating>) {
        this.ratingChangeList = ratingChangeList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtContestName: TextView = itemView.txtName
        private val txtRank: TextView = itemView.txtUserRank
        private val txtRatingUpdateTime: TextView = itemView.txtRatingUpdateTime
        private val txtRatingChange: TextView = itemView.txtRatingChange
        fun bind(rating: RatingChange.Rating) {
            txtContestName.text = rating.contestName
            txtRank.text =
                SpannableStringBuilder().bold { append("Rank : ") }.append(rating.rank.toString())
            txtRatingUpdateTime.text = SpannableStringBuilder().bold { append("Updated On : ") }
                .append(convertEpochToStringDate(rating.ratingUpdateTimeSeconds))
            if (rating.newRating - rating.oldRating > 0) {
                txtRatingChange.text = "+${rating.newRating - rating.oldRating}"
                txtRatingChange.setTextColor(itemView.context.resources.getColor(R.color.passed))
            } else {
                txtRatingChange.text = "${rating.newRating - rating.oldRating}"
                txtRatingChange.setTextColor(itemView.context.resources.getColor(R.color.failed))
            }
        }
    }
}