package com.tsunderead.tsundoku.api

import android.util.Log

interface NetworkCaller<T> {
    fun onCallSuccess(result: T?)
    fun onCallFail() {
        Log.e("NetworkCaller", "Network Call Failed")
    }
}