package com.jaylangkung.e_jartas.utils.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Logger")
data class Logger(
    @ColumnInfo(name = "context") val context: String,
    @ColumnInfo(name = "func") val func: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "time") val time: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
