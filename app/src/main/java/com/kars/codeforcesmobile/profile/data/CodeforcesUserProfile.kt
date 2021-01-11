package com.kars.codeforcesmobile.profile.data

data class CodeforcesUserProfile(
    val status: String,
    val result: List<Profile>?,
    val comment: String?
){
    data class Profile(
        val contribution: Int,
        val rating: Int,
        val titlePhoto: String,
        val rank: String,
        val handle: String
    )
}