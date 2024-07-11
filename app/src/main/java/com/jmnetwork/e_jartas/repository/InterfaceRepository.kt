package com.jmnetwork.e_jartas.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.jmnetwork.e_jartas.model.DasboardModel
import com.jmnetwork.e_jartas.model.DefaultResponse
import com.jmnetwork.e_jartas.model.LoginRequest
import com.jmnetwork.e_jartas.model.LoginResponse
import com.jmnetwork.e_jartas.model.ProviderRequest
import com.jmnetwork.e_jartas.model.ProviderResponse
import com.jmnetwork.e_jartas.model.RuasJalanRequest
import com.jmnetwork.e_jartas.model.RuasJalanResponse
import com.jmnetwork.e_jartas.model.SpinnerResponse

interface BaseRepository {
    fun login(context: Context, loginRequest: LoginRequest, deviceID: String, tokenAuth: String): LiveData<LoginResponse>
    fun getDashboard(context: Context, tokenAuth: String): LiveData<DasboardModel>
}

interface ManajemenJalanRepository {
    fun getSpinnerData(context: Context, varian: String, tokenAuth: String): LiveData<SpinnerResponse>
    fun getRuasJalan(context: Context, limit: Int, page: Int, tokenAuth: String): LiveData<RuasJalanResponse>
    fun addRuasJalan(context: Context, idadmin: Int, requestData: RuasJalanRequest, tokenAuth: String): LiveData<DefaultResponse>
    fun editRuasJalan(context: Context, idadmin: Int, iddata: Int, requestData: RuasJalanRequest, tokenAuth: String): LiveData<DefaultResponse>
    fun deleteRuasJalan(context: Context, idadmin: Int, iddata: Int, tokenAuth: String): LiveData<DefaultResponse>
}

interface ManajemenTiangRepository {
    fun getProvider(context: Context, limit: Int, page: Int, tokenAuth: String): LiveData<ProviderResponse>
    fun addProvider(context: Context, idadmin: Int, requestData: ProviderRequest, tokenAuth: String): LiveData<DefaultResponse>
    fun blacklistProvider(context: Context, idadmin: Int, iddata: Int, isBlacklist: Boolean, tokenAuth: String): LiveData<DefaultResponse>
//    fun editProvider(context: Context, idadmin: Int, iddata: Int, requestData: ProviderRequest, tokenAuth: String): LiveData<DefaultResponse>
//    fun deleteProvider(context: Context, idadmin: Int, iddata: Int, tokenAuth: String): LiveData<DefaultResponse>
}