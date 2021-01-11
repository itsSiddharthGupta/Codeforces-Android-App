package com.kars.codeforcesmobile.profile.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.kars.codeforcesmobile.ApiService
import com.kars.codeforcesmobile.profile.data.CodeforcesUserProfile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ProfileViewModel : ViewModel() {
    var userProfile: MutableLiveData<CodeforcesUserProfile> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getUserProfile(handle: String, retrofit: Retrofit) {
        isLoading.value = true
        retrofit.create(ApiService::class.java).getUserProfile(handle)
            .enqueue(object : Callback<CodeforcesUserProfile> {
                override fun onResponse(
                    call: Call<CodeforcesUserProfile>,
                    response: Response<CodeforcesUserProfile>
                ) {
                    if(response.isSuccessful && response.body()!=null)
                        userProfile.value = response.body()!!
                    else{
                        userProfile.value = Gson().fromJson(response.errorBody()?.string(), CodeforcesUserProfile::class.java)
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<CodeforcesUserProfile>, t: Throwable) {
                    // TODO("Not yet implemented")
                    isLoading.value = false
                }
            })
    }
}