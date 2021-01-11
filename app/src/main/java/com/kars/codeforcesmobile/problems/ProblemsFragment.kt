package com.kars.codeforcesmobile.problems

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.*
import com.kars.codeforcesmobile.R
import com.kars.codeforcesmobile.contest.ContestModel
import com.kars.codeforcesmobile.contest.NotificationWorker
import com.kars.codeforcesmobile.convertEpochToStringDate
import kotlinx.android.synthetic.main.problems_fragment.*
import java.util.concurrent.TimeUnit

class ProblemsFragment : Fragment() {
    private lateinit var viewModel: ProblemsViewModel
    private var clicks : Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.problems_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProblemsViewModel::class.java)
        val listWorkInfo: List<WorkInfo> = WorkManager.getInstance(requireContext()).getWorkInfosByTag("Clicks").get()
        Log.e("Works", listWorkInfo.toString())
        Log.e("WorkSize", listWorkInfo.size.toString())
        set_alert_button.setOnClickListener {
            val contest = ContestModel(clicks, "Hello $clicks", "", "", "23", (System.currentTimeMillis() + 100000).toString())
            clicks++
            Log.e("Notific", "$contest $clicks")
            setNotificationWork(contest)
        }
    }

    private fun setNotificationWork(contestModel: ContestModel){
        //todo bug see ss in phone all notification ring on last notification time
        val delay = contestModel.startTimeSeconds.toLong() - System.currentTimeMillis()
        Log.e("Delay", delay.toString())
        val data = Data.Builder()
            .putString("CONTEST-NAME", contestModel.name)
            .putString("CONTEST-TIME", convertEpochToStringDate(contestModel.startTimeSeconds.toLong()))
            .putInt("CONTEST-ID", contestModel.id)
            .build()
        val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).addTag("Clicks").build()
        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager.beginUniqueWork(NotificationWorker.NOTIFICATION_WORK + contestModel.id, ExistingWorkPolicy.KEEP, notificationWork).enqueue()
    }

}