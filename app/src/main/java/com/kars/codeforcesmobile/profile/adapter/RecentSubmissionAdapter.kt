package com.kars.codeforcesmobile.profile.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kars.codeforcesmobile.R
import com.kars.codeforcesmobile.convertEpochToStringDate
import com.kars.codeforcesmobile.profile.data.CodeforcesSubmissions
import kotlinx.android.synthetic.main.submission_item.view.*

class RecentSubmissionAdapter : RecyclerView.Adapter<RecentSubmissionAdapter.MyViewHolder>() {
    private var submissionsList: MutableList<CodeforcesSubmissions.Submission> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = View.inflate(parent.context, R.layout.submission_item, null)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(submissionsList[position])
    }

    override fun getItemCount() = submissionsList.size

    fun fillData(submissions: List<CodeforcesSubmissions.Submission>) {
        this.submissionsList.addAll(submissions)
        Log.e("size", submissionsList.size.toString())
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtProblemName: TextView = itemView.txtProblemName
        private val txtInfo: TextView = itemView.txtInfo
        private val txtSubmissionDate: TextView = itemView.txtSubmissionTime
        private val txtVerdict: TextView = itemView.txtVerdict
        fun bind(submission: CodeforcesSubmissions.Submission) {
            txtProblemName.text = submission.problem.name
            txtInfo.text =
                StringBuilder().append(submission.passedTestCount).append(" cases passed, ")
                    .append(submission.timeConsumedMillis).append("ms, ")
                    .append(submission.programmingLanguage).toString()
            txtSubmissionDate.text = convertEpochToStringDate(submission.creationTimeSeconds)
            txtVerdict.text = submission.verdict
            if(submission.verdict == "OK"){
                txtVerdict.setTextColor(itemView.context.resources.getColor(R.color.passed))
            } else {
                txtVerdict.setTextColor(itemView.context.resources.getColor(R.color.failed))
            }
        }
    }
}