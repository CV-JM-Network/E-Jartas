package com.jmnetwork.e_jartas.utils

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jmnetwork.e_jartas.R
import com.jmnetwork.e_jartas.utils.room.Logger
import com.jmnetwork.e_jartas.utils.room.LoggerDatabase
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Calendar
import java.util.Date

class CustomHandler {

    private lateinit var loggerDatabase: LoggerDatabase
    private val crashlytics = FirebaseCrashlytics.getInstance()

    fun responseHandler(context: Context, func: String, message: String) {
        val now: Date = Calendar.getInstance().time
        when {
            message.contains("failed to connect to", ignoreCase = true) -> {
                Toasty.error(context, "Terdapat permasalahan pada server", Toasty.LENGTH_LONG).show()
                crashlytics.log("Server connection failed in $func")
                crashlytics.recordException(Exception("Server connection failed"))
            }

            message.contains("Unable to resolve host") -> {
                Toasty.error(context, "Silahkan cek koneksi internet Anda", Toasty.LENGTH_LONG).show()
                crashlytics.log("Unable to resolve host in $func")
                crashlytics.recordException(Exception("Unable to resolve host"))
            }

            message.contains("Forbidden") -> {
                Toasty.error(context, "File yang Anda upload tidak didukung", Toasty.LENGTH_LONG).show()
                crashlytics.log("Forbidden file upload in $func")
                crashlytics.recordException(Exception("Forbidden file upload"))
            }

            message.contains("Unauthorized") -> {
                Toasty.warning(context, "Unauthorized access", Toasty.LENGTH_LONG).show()
                crashlytics.log("Unauthorized access")
                crashlytics.recordException(Exception("Unauthorized access"))
            }

            else -> {
                Toasty.error(context, R.string.try_again, Toasty.LENGTH_LONG).show()
                crashlytics.log("Unknown error in $func: $message")
                crashlytics.recordException(Exception("Unknown error: $message"))
            }
        }

        GlobalScope.launch {
            insertDB(context, func, message, now.toString())
        }
        Log.e("Logger", "context : $context, fun : $func, message : $message, time : $now")
    }

    fun parseError(message: String): String {
        val errJson = JSONObject(message)
        return errJson.optString("message", "Error")
    }

    private fun insertDB(context: Context, func: String, message: String, time: String) {
        loggerDatabase = Room.databaseBuilder(context, LoggerDatabase::class.java, "logger.db").build()
        loggerDatabase.loggerDao().insert(
            Logger(context.toString(), func, message, time)
        )
    }
}