package com.jmnetwork.e_jartas.utils

import android.content.Context
import androidx.room.Room
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.ktx.Firebase
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
    private val crashlytics = Firebase.crashlytics

    fun responseHandler(context: Context, func: String, message: String = "", code: Int = 200) {
        val now: Date = Calendar.getInstance().time
        val ctx = context.toString()
        when {
            message.contains("failed to connect to") -> {
                Toasty.error(context, "Terdapat permasalahan pada server", Toasty.LENGTH_LONG).show()
                crashlytics.recordException(Exception("Server connection failed"))
            }

            message.contains("Unable to resolve host") -> {
                Toasty.error(context, "Silahkan cek koneksi internet Anda", Toasty.LENGTH_LONG).show()
                crashlytics.log("Unable to resolve host in $func, context: $ctx")
                crashlytics.recordException(Exception("Unable to resolve host"))
            }

            message.contains("Forbidden") -> {
                Toasty.error(context, "File yang Anda upload tidak didukung", Toasty.LENGTH_LONG).show()
                crashlytics.log("Forbidden file upload in $func, context: $ctx")
            }

            message.contains("Unauthorized") -> {
                crashlytics.setCustomKeys {
                    key("context", ctx)
                    key("function", func)
                    key("message", message)
                }
            }

            code == 400 -> {
                val msg = parseError(message)
                Toasty.error(context, msg, Toasty.LENGTH_LONG).show()
                crashlytics.recordException(Exception("Bad request: $msg, context: $ctx"))
            }

            code == 500 -> {
                Toasty.error(context, "Internal server error", Toasty.LENGTH_LONG).show()
                crashlytics.recordException(Exception("Internal server error, context: $ctx"))
            }

            else -> {
                Toasty.error(context, R.string.try_again, Toasty.LENGTH_LONG).show()
                crashlytics.recordException(Exception("General error: $message, context: $ctx"))
            }
        }

        GlobalScope.launch {
            insertDB(context, func, message, now.toString())
        }
//        Log.e("Logger", "context : $context, fun : $func, message : $message, time : $now")
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