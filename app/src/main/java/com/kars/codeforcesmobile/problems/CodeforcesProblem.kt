package com.kars.codeforcesmobile.problems

data class CodeforcesProblem(
    val contestId: Long,
    val index: String,
    val name: String,
    val type: String,
    val points: Float,
    val rating: Int,
    val tags: List<String>
)