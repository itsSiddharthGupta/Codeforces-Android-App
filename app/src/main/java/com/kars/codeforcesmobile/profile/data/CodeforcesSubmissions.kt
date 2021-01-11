package com.kars.codeforcesmobile.profile.data

import com.kars.codeforcesmobile.problems.CodeforcesProblem

data class CodeforcesSubmissions(
    val status: String,
    val result: List<Submission>
){
    data class Submission(
        val id: Long,
        val contestId: Long,
        val creationTimeSeconds: Long,
        val problem: CodeforcesProblem,
        val programmingLanguage: String,
        val verdict: String,
        val passedTestCount: Int,
        val timeConsumedMillis: Long
    )
}