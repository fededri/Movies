package com.fedetorres.movies.network

import android.content.Context
import com.fedetorres.movies.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ApiErrorParser @Inject constructor(val context: Context) {

    fun parseError(error: Throwable): String? {
        return when (error) {
            is HttpException -> {
                error.response().errorBody()?.string()
            }
            is SocketTimeoutException -> context.getString(R.string.timeout)
            else -> context.getString(R.string.unknown_error)
        }
    }


}