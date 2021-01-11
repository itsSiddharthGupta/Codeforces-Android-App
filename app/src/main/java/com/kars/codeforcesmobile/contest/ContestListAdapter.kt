package com.kars.codeforcesmobile.contest

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.kars.codeforcesmobile.R
import com.kars.codeforcesmobile.convertEpochToStringDate
import com.kars.codeforcesmobile.convertSecondsToHours
import kotlinx.android.synthetic.main.layout_contest_adapter.view.*

class ContestListAdapter :
    RecyclerView.Adapter<ContestListAdapter.MyViewHolder>() {
    private var contestList: List<ContestModel> = ArrayList<ContestModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_contest_adapter, null)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = params
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(contestList[position])
    }

    override fun getItemCount(): Int = contestList.size

    fun populateList(contestList: List<ContestModel>) {
        this.contestList = contestList;
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var txtName: TextView = itemView.txtName
        private var txtDate: TextView = itemView.txtStartTime
        private var txtDuration: TextView = itemView.txtDuration
//        private var fabAddReminder: FloatingActionButton = itemView.fabAddReminder

        fun bindView(contest: ContestModel) {
            txtName.text = contest.name
            txtDate.text = SpannableStringBuilder().bold { append("Starts On : ") }
                .append(convertEpochToStringDate(contest.startTimeSeconds.toLong()))
            txtDuration.text = SpannableStringBuilder().bold { append("Duration : ") }
                .append(convertSecondsToHours(contest.durationSeconds.toLong()))
//            fabAddReminder.setOnClickListener { reminderListener.onReminderClick(contest) }
        }
    }

//    interface OnReminderListener {
//        fun onReminderClick(contest: ContestModel)
//    }
}