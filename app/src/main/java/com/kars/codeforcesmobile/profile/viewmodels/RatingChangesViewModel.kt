package com.kars.codeforcesmobile.profile.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kars.codeforcesmobile.ApiService
import com.kars.codeforcesmobile.profile.data.RatingChange
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RatingChangesViewModel : ViewModel() {
    var ratingChangeList: MutableLiveData<List<RatingChange.Rating>> = MutableLiveData()
    fun getRatingChanges(handle: String, client: Retrofit){
        client.create(ApiService::class.java).getUserRatingList(handle)
            .enqueue(object : Callback<RatingChange>{
                override fun onResponse(call: Call<RatingChange>, response: Response<RatingChange>) {
                    if(response.isSuccessful && response.body()!=null){
                        if(response.body()!!.status == "OK"){
                            ratingChangeList.value = response.body()!!.result
                        }
                    }
                }

                override fun onFailure(call: Call<RatingChange>, t: Throwable) {

                }
            })
    }
}