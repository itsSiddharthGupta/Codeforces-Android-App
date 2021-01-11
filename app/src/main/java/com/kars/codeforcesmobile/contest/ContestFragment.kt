package com.kars.codeforcesmobile.contest

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.kars.codeforcesmobile.*
import kotlinx.android.synthetic.main.contest_fragment.*
import kotlinx.android.synthetic.main.contest_fragment.view.*
import retrofit2.Retrofit

//todo if notifications are enabled set the notification of upcoming contest without making user to open app.
class ContestFragment : Fragment() {
    private lateinit var client: Retrofit
    private lateinit var adapter: ContestListAdapter
    private lateinit var viewModel: ContestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = RetrofitClient.retrofitClientCodeForces!!
        viewModel = ViewModelProvider(this).get(ContestViewModel::class.java)
        viewModel.getContestList(client)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contest_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContestListAdapter()
        view.recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.adapter = adapter
        setEnableNotificationMsg()

        viewModel.contestList.observe(viewLifecycleOwner, Observer { list ->
            if (list != null) {
                val filtered = list.filter { it.phase == "BEFORE" }
                val finalReversedList = filtered.asReversed()
                val upcomingContest = finalReversedList[0]
                populateUpcomingContest(upcomingContest)
                adapter.populateList(finalReversedList.subList(1, finalReversedList.size))
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    shimmerContest.visibility = View.VISIBLE
                    contentLayout.visibility = View.GONE
                } else {
                    shimmerContest.visibility = View.GONE
                    contentLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setEnableNotificationMsg() {
        val isNotificationEnabled: Boolean? =
            context?.getSharedPreferences("user_preferences", 0)?.getBoolean("Notification", false)
        Log.e("NotificationSetting", isNotificationEnabled.toString())
        if (isNotificationEnabled != null && isNotificationEnabled) {
            notificationEnableMsg.text =
                "You will get notification of the contest an hour before it starts!"
        } else {
            notificationEnableMsg.text =
                "Enable notification from preferences to get notification of the contest beforehand!"
        }
    }

    private fun setNotificationForUpcomingContest(contestModel: ContestModel) {
        val isNotificationEnabled: Boolean? =
            context?.getSharedPreferences(USER_PREFERENCES, 0)?.getBoolean(
                USER_PREFERENCES_NOTIFICATION, false
            )
        // Work manager tasks list
        val listWorkInfo = WorkManager.getInstance(requireContext()).getWorkInfosForUniqueWork(
            NotificationWorker.NOTIFICATION_WORK + contestModel.id
        ).get()
        Log.e("ContestWorks", listWorkInfo.toString())
        if (isNotificationEnabled != null && isNotificationEnabled) {
            if (listWorkInfo.size == 0) {
                viewModel.setNotificationWork(
                    contestModel,
                    System.currentTimeMillis(),
                    requireContext()
                )
            }
        }
    }

    private fun populateUpcomingContest(contest: ContestModel) {
        txtName.text = contest.name
        txtStartTime.text = SpannableStringBuilder().bold { append("Starts On : ") }
            .append(convertEpochToStringDate(contest.startTimeSeconds.toLong()))
        txtDuration.text = SpannableStringBuilder().bold { append("Duration : ") }
            .append(convertSecondsToHours(contest.durationSeconds.toLong()))
        setNotificationForUpcomingContest(contest)
    }

//    override fun onReminderClick(contest: ContestModel) {
//        viewModel.setNotificationWork(contest, Date().time, requireContext())
//    }
}