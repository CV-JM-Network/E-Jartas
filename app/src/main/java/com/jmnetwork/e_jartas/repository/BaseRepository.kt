package com.jmnetwork.e_jartas.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.jmnetwork.e_jartas.model.DasboardModel
import com.jmnetwork.e_jartas.model.LoginRequest
import com.jmnetwork.e_jartas.model.LoginResponse
import com.jmnetwork.e_jartas.model.RuasJalanResponse

interface BaseRepository {
    fun login(context: Context, loginRequest: LoginRequest, deviceID: String, tokenAuth: String): LiveData<LoginResponse>
    fun getDashboard(context: Context, tokenAuth: String): LiveData<DasboardModel>
    fun getRuasJalan(context: Context, limit: Int, page: Int, tabel: String, tokenAuth: String): LiveData<RuasJalanResponse>
}