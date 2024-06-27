package com.jmnetwork.e_jartas.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.jmnetwork.e_jartas.model.DasboardModel
import com.jmnetwork.e_jartas.model.DefaultResponse
import com.jmnetwork.e_jartas.model.LoginRequest
import com.jmnetwork.e_jartas.model.LoginResponse
import com.jmnetwork.e_jartas.model.RuasJalanRequest
import com.jmnetwork.e_jartas.model.RuasJalanResponse
import com.jmnetwork.e_jartas.model.SpinnerResponse

interface BaseRepository {
    fun login(context: Context, loginRequest: LoginRequest, deviceID: String, tokenAuth: String): LiveData<LoginResponse>
    fun getDashboard(context: Context, tokenAuth: String): LiveData<DasboardModel>
}

interface ManajemenJalanRepository {
    fun getSpinnerData(context: Context, varian: String, tokenAuth: String): LiveData<SpinnerResponse>
    fun getRuasJalan(context: Context, limit: Int, page: Int, tabel: String, tokenAuth: String): LiveData<RuasJalanResponse>
    fun addRuasJalan(context: Context, idadmin: Int, requestData: RuasJalanRequest, tokenAuth: String): LiveData<DefaultResponse>
    fun editRuasJalan(context: Context, idadmin: Int, iddata: Int, requestData: RuasJalanRequest, tokenAuth: String): LiveData<DefaultResponse>
    fun deleteRuasJalan(context: Context, idadmin: Int, iddata: Int, tokenAuth: String): LiveData<DefaultResponse>
}