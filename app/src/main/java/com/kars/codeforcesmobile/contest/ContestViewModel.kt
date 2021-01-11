package com.kars.codeforcesmobile.contest

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.kars.codeforcesmobile.ApiService
import com.kars.codeforcesmobile.contest.NotificationWorker.Companion.NOTIFICATION_WORK
import com.kars.codeforcesmobile.convertEpochToStringDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class ContestViewModel : ViewModel() {
    var contestList: MutableLiveData<List<ContestModel>> = MutableLiveData()
    var gymFilter: MutableLiveData<Boolean> = MutableLiveData(false)
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    fun getContestList(client: Retrofit) {
        isLoading.value = true
        client.create(ApiService::class.java).getContestList(gymFilter.value!!).enqueue(object :
            Callback<CodeforcesContestResponse> {
            override fun onResponse(
                call: Call<CodeforcesContestResponse>,
                response: Response<CodeforcesContestResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    contestList.value = response.body()!!.result
                }
                isLoading.value = false
            }

            override fun onFailure(call: Call<CodeforcesContestResponse>, t: Throwable) {
                isLoading.value = false
            }

        })
    }

    fun setNotificationWork(
        contestModel: ContestModel,
        currentTimeInMillis: Long,
        context: Context
    ) {
        val delay =
            contestModel.startTimeSeconds.toLong().times(1000) - currentTimeInMillis - 3600000
        val data = Data.Builder()
            .putString("CONTEST-NAME", contestModel.name)
            .putString(
                "CONTEST-TIME",
                convertEpochToStringDate(contestModel.startTimeSeconds.toLong())
            )
            .putInt("CONTEST-ID", contestModel.id)
            .build()
        val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data)
            .addTag("upcoming-contest").build()
        val instanceWorkManager = WorkManager.getInstance(context)
        instanceWorkManager.beginUniqueWork(
            NOTIFICATION_WORK + contestModel.id,
            ExistingWorkPolicy.REPLACE,
            notificationWork
        ).enqueue()
    }
}