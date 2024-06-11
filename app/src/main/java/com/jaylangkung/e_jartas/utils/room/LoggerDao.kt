package com.jaylangkung.e_jartas.utils.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jaylangkung.e_jartas.utils.room.Logger

@Dao
interface LoggerDao {
    @Query("SELECT * FROM Logger ORDER BY id DESC")
    fun getAllLog(): List<Logger>

    @Insert
    fun insert(log: Logger)
}