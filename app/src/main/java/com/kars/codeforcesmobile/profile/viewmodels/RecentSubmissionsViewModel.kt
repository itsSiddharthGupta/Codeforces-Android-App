package com.kars.codeforcesmobile.profile.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kars.codeforcesmobile.ApiService
import com.kars.codeforcesmobile.profile.data.CodeforcesSubmissions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RecentSubmissionsViewModel : ViewModel() {
    var submissionsList: MutableLiveData<List<CodeforcesSubmissions.Submission>> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var isLastPage: MutableLiveData<Boolean> = MutableLiveData(false)
    var from: MutableLiveData<Int> = MutableLiveData(1)
    fun getSubmissions(userHandle: String, count: Int, retrofit: Retrofit) {
        isLoading.value = true
        viewModelScope.launch {
            delay(2000)
            retrofit.create(ApiService::class.java).getUserRecentSubmissions(
                userHandle,
                from.value!!,
                count
            ).enqueue(object : Callback<CodeforcesSubmissions> {
                override fun onResponse(
                    call: Call<CodeforcesSubmissions>,
                    response: Response<CodeforcesSubmissions>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        submissionsList.value = response.body()!!.result
                        if (submissionsList.value!!.isEmpty()) {
                            isLastPage.value = true
                        }
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<CodeforcesSubmissions>, t: Throwable) {
                    isLoading.value = false
                }
            })
        }
    }
}