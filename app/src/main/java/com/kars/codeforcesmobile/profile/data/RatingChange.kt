package com.kars.codeforcesmobile.profile.data

data class RatingChange (
    val status: String,
    val result: List<Rating>
){
    data class Rating(
        val contestId: Int,
        val contestName: String,
        val rank: Int,
        val ratingUpdateTimeSeconds: Long,
        val oldRating: Int,
        val newRating: Int
    )
}