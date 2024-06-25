package com.jmnetwork.e_jartas.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jmnetwork.e_jartas.model.DasboardModel
import com.jmnetwork.e_jartas.model.DashboardResponse
import com.jmnetwork.e_jartas.model.LoginRequest
import com.jmnetwork.e_jartas.model.LoginResponse
import com.jmnetwork.e_jartas.model.RuasJalanResponse
import com.jmnetwork.e_jartas.utils.CustomHandler
import retrofit2.Call
import retrofit2.Response

class BaseRepositoryImpl : BaseRepository {
    private val apiService = RetrofitClient.apiService

    override fun login(
        context: Context,
        loginRequest: LoginRequest,
        deviceID: String,
        tokenAuth: String
    ): LiveData<LoginResponse> {
        val loginData = MutableLiveData<LoginResponse>()

        RetrofitClient.apiService.login(
            loginRequest.email,
            loginRequest.password,
            deviceID,
            tokenAuth,
        ).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    loginData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "Login|onResponse", response.message())
                    loginData.postValue(
                        LoginResponse(null, response.message(), "error", "")
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "Login|onFailure", t.message.toString())
                loginData.postValue(
                    LoginResponse(null, t.message.toString(), "error", "")
                )
            }
        })

        return loginData
    }

    override fun getDashboard(context: Context, tokenAuth: String): LiveData<DasboardModel> {
        val dashboardData = MutableLiveData<DasboardModel>()

        apiService.dashboard(tokenAuth).enqueue(object : retrofit2.Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                when (response.code()) {
                    200 -> {
                        val data = response.body()!!.data
                        dashboardData.postValue(data)
                    }

                    500 -> {
                        CustomHandler().responseHandler(context, "getDashboard|onResponse", response.message(), 500)
                    }

                    else -> {
                        CustomHandler().responseHandler(context, "getDashboard|onResponse", response.message(), response.code())
                    }
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "getDashboard|onFailure", t.message.toString())
            }
        })

        return dashboardData
    }

    override fun getRuasJalan(
        context: Context,
        limit: Int,
        page: Int,
        tabel: String,
        tokenAuth: String
    ): LiveData<RuasJalanResponse> {
        val ruasJalanData = MutableLiveData<RuasJalanResponse>()

        apiService.getRuasJalan(limit, page, tabel, tokenAuth).enqueue(object : retrofit2.Callback<RuasJalanResponse> {
            override fun onResponse(call: Call<RuasJalanResponse>, response: Response<RuasJalanResponse>) {
                when (response.code()) {
                    200 -> {
                        ruasJalanData.postValue(response.body())
                    }

                    500 -> {
                        CustomHandler().responseHandler(context, "getRuasJalan|onResponse", response.message(), 500)
                    }

                    else -> {
                        CustomHandler().responseHandler(context, "getRuasJalan|onResponse", response.message(), response.code())
                    }
                }
            }

            override fun onFailure(call: Call<RuasJalanResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "getRuasJalan|onFailure", t.message.toString())
            }
        })

        return ruasJalanData
    }
}