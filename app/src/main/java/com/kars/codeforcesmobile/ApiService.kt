package com.kars.codeforcesmobile

import com.kars.codeforcesmobile.contest.CodeforcesContestResponse
import com.kars.codeforcesmobile.profile.data.CodeforcesSubmissions
import com.kars.codeforcesmobile.profile.data.CodeforcesUserProfile
import com.kars.codeforcesmobile.profile.data.RatingChange
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("contest.list/")
    fun getContestList(@Query("gym") includeGym: Boolean): Call<CodeforcesContestResponse>

    @GET("user.rating/")
    fun getUserRatingList(@Query("handle") handle: String): Call<RatingChange>

    @GET("user.status/")
    fun getUserRecentSubmissions(
        @Query("handle") handle: String,
        @Query("from") from: Int,
        @Query("count") count: Int
    ): Call<CodeforcesSubmissions>

    @GET("user.info/")
    fun getUserProfile(
        @Query("handles") handle: String
    ): Call<CodeforcesUserProfile>
}