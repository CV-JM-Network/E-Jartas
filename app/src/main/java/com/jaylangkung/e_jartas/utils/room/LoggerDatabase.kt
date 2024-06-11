package com.jaylangkung.e_jartas.utils.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Logger::class], version = 1)
abstract class LoggerDatabase : RoomDatabase() {
    abstract fun loggerDao(): LoggerDao

    companion object {
        @Volatile
        private var INSTANCE: LoggerDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): LoggerDatabase {
            if (INSTANCE == null) {
                synchronized(LoggerDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, LoggerDatabase::class.java, "bpkpd_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}