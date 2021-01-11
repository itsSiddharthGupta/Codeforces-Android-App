package com.kars.codeforcesmobile

import java.text.SimpleDateFormat
import java.util.*

const val CODEFORCES_BASE_URL = "https://codeforces.com/api/"
const val USER_PREFERENCES = "user_preferences"
const val USER_PREFERENCES_NOTIFICATION = "notification"
fun convertEpochToStringDate(epoch: Long): String{
    val date = Date(epoch*1000)
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy, hh:mm aa", Locale.ENGLISH)
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun convertSecondsToHours(_sec: Long): String{
    var sec = _sec
    val hrs = sec/3600
    sec %= 3600
    val min = sec/60
    return "${String.format("%02d", hrs)}:${String.format("%02d", min)} hours"
}