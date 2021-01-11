package com.kars.codeforcesmobile.contest

data class ContestModel(
    val id: Int,
    val name: String,
    val type: String,
    val phase: String,
    val durationSeconds: String,
    val startTimeSeconds: String
)